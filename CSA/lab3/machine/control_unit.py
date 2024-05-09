from __future__ import annotations

import logging

from machine.data_path import DataPath, Enum, Selector
from machine.isa import HaltError, Instruction, Opcode, TypesOfAddressing


class States(Enum):
    SWITCH_CR = "SWITCH_CR"
    SWITCH_IP = "SWITCH_IP"
    SWITCH_AR = "SWITCH_AR"
    SWITCH_AR_FROM_MEMORY = "SWITCH_AR_FROM_MEMORY"
    SWITCH_DR = "SWITCH_DR"
    EXECUTE_COMMAND = "EXECUTE_COMMAND"


non_address_commands: list[Opcode] = [Opcode.PUSH, Opcode.POP, Opcode.INC, Opcode.HALT]
math_commands: list[Opcode] = [Opcode.ADD, Opcode.MUL, Opcode.SUB, Opcode.DIV, Opcode.INC]
jump_commands: list[Opcode] = [Opcode.JUMP, Opcode.JE, Opcode.JA, Opcode.JB, Opcode.JNE]


class ControlUnit:
    def __init__(self, data_path: DataPath, tick_limit: int):
        self.data_path = data_path
        self.state: States = States.SWITCH_CR
        self.tick_counter = 0
        self.tick_limit = tick_limit

    def instruction_fetch(self):
        self.data_path.latch_cr()
        self.data_path.latch_ip(Selector.FROM_IP)

    def operand_fetch(self):
        type_of_address = self.data_path.cr.arg1
        if self.state == States.SWITCH_AR and type_of_address[0] == TypesOfAddressing.DIRECT.value:
            return
        if self.state == States.SWITCH_AR and type_of_address[0] == TypesOfAddressing.ABSOLUT.value:
            self.data_path.latch_ar(Selector.FROM_CR)
        if self.state == States.SWITCH_AR and type_of_address[0] == TypesOfAddressing.RELATIVE_REG.value:
            self.data_path.latch_ar(Selector.FROM_CR)
        if self.state == States.SWITCH_AR_FROM_MEMORY:
            self.data_path.latch_ar(Selector.FROM_MEMORY)
        if self.state == States.SWITCH_DR and type_of_address[0] == TypesOfAddressing.DIRECT.value:
            self.data_path.latch_dr(Selector.FROM_CR)
        elif self.state == States.SWITCH_DR and self.data_path.cr.opcode != Opcode.ST:
            self.data_path.latch_dr(Selector.FROM_MEMORY)

    def switch_state(self):
        if self.state == States.SWITCH_CR:
            self.state = (
                States.SWITCH_AR if self.data_path.cr.opcode not in non_address_commands else States.EXECUTE_COMMAND
            )
            return

        if self.state == States.SWITCH_AR and self.data_path.cr.arg1[0] == TypesOfAddressing.RELATIVE_REG.value:
            self.state = States.SWITCH_AR_FROM_MEMORY
            return

        if self.state == States.SWITCH_AR or self.state == States.SWITCH_AR_FROM_MEMORY:
            self.state = States.SWITCH_DR
            return

        if self.state == States.SWITCH_DR:
            self.state = States.EXECUTE_COMMAND
            return

        if self.state == States.EXECUTE_COMMAND:
            self.state = States.SWITCH_CR

    def process_jump_commands(self, opcode: Opcode, zero_flag, negative_flag):
        if (
            opcode == Opcode.JE
            and zero_flag == 1
            or opcode == Opcode.JA
            and (negative_flag == 0)
            or opcode == Opcode.JUMP
            or opcode == Opcode.JB
            and (negative_flag == 1 or zero_flag == 1)
            or opcode == Opcode.JNE
            and zero_flag == 0
        ):
            self.data_path.latch_ip(Selector.FROM_DR)

    def process_non_address_commands(self, opcode: Opcode):
        if opcode == Opcode.HALT:
            self.data_path.device.output_the_buffer()
            raise HaltError
        if opcode == Opcode.PUSH:
            self.data_path.signal_wr(Selector.FROM_SP)
            self.data_path.latch_sp(Selector.SEL_DEC)
        if opcode == Opcode.POP:
            self.data_path.latch_sp(Selector.SEL_INC)
            self.data_path.latch_acc(Selector.FROM_STACK)

    def execute_command(self):
        opcode = self.data_path.cr.opcode

        if opcode == Opcode.LD:
            self.data_path.latch_acc(Selector.FROM_DR)
        if opcode in math_commands:
            self.data_path.latch_left_op()
            self.data_path.latch_right_op()
            self.data_path.calculate(opcode)
            self.data_path.latch_acc(Selector.FROM_ALU)

        if opcode == Opcode.ST:
            self.data_path.signal_wr(Selector.FROM_AR)
        if opcode == Opcode.CMP:
            self.data_path.latch_left_op()
            self.data_path.latch_right_op()
            self.data_path.calculate(opcode)
        if opcode in jump_commands:
            zero_flag, negative_flag = self.data_path.get_flags()
            self.process_jump_commands(opcode, zero_flag, negative_flag)
        if opcode in non_address_commands:
            self.process_non_address_commands(opcode)

    def process_tick(self):
        state = self.state
        if state == States.SWITCH_CR:
            self.instruction_fetch()
        if state == States.SWITCH_DR or state == States.SWITCH_AR or state == States.SWITCH_AR_FROM_MEMORY:
            self.operand_fetch()
        if state == States.EXECUTE_COMMAND:
            self.execute_command()
            logging.debug("%s", self)
        self.switch_state()
        self.tick_counter += 1

    def __repr__(self):
        return "execute_command {:>15} | tick: {:10d} | ip: {:10d} | dr {:10d} |ar: {:10d} | acc: {:10d}".format(
            self.data_path.cr.opcode,
            self.tick_counter,
            self.data_path.ip,
            self.data_path.dr.arg1 if isinstance(self.data_path.dr, Instruction) else self.data_path.dr,
            self.data_path.ar,
            self.data_path.acc,
        )
