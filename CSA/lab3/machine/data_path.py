from __future__ import annotations

import logging
from typing import ClassVar

from machine.isa import (
    DIVISION_BY_ZERO,
    INPUT_OUTPUT_BUFFER_END,
    INPUT_OUTPUT_BUFFER_START,
    MEMORY_SIZE,
    Enum,
    Instruction,
    Opcode,
)


class AluOpcode(Enum):
    ADD = "ADD"
    SUB = "SUB"
    MUL = "MUL"
    DIV = "DIV"
    CMP = "CMP"
    INC = "INC"


opcode_to_alu_opcode: dict[Opcode, AluOpcode] = {
    Opcode.ADD: AluOpcode.ADD,
    Opcode.SUB: AluOpcode.SUB,
    Opcode.DIV: AluOpcode.DIV,
    Opcode.MUL: AluOpcode.MUL,
    Opcode.CMP: AluOpcode.CMP,
    Opcode.INC: AluOpcode.INC,
}


class Selector(Enum):
    FROM_DR = "FROM_DR"
    FROM_MEMORY = "FROM_MEMORY"
    FROM_STACK = "FROM_STACK"
    FROM_ACC = "FROM_ACC"
    FROM_ALU = "FROM_ALU"
    SEL_0 = "SEL_0"
    SEL_INC = "SEL_INC"
    SEL_DEC = "SEL_DEC"
    FROM_IP = "FROM_IP"
    FROM_CR = "FROM_CR"
    FROM_AR = "FROM_AR"
    FROM_SP = "FROM_SP"


class Device:
    output_buffer: ClassVar[list] = []
    input_buffer: ClassVar[list] = []

    def __init__(self, input_file: str):
        self.start_buffer_pointer = INPUT_OUTPUT_BUFFER_START
        self.end_buffer_pointer = INPUT_OUTPUT_BUFFER_END
        self.output_type = "str"
        self.input_buffer_pointer = -1
        with open(input_file, encoding="utf-8") as f:
            text = f.read()
            self.input_buffer = [c for c in text]
        self.input_buffer.append("0")

    def output_the_buffer(self):
        for c in self.output_buffer:
            print(c, end="")
        logging.debug("output: " + "".join(self.output_buffer))
        self.output_buffer.clear()

    def output(self, value: int) -> None:
        if self.output_type == "str":
            if value == 0:
                return
            if chr(value) == " ":
                logging.debug("".join(self.output_buffer) + " <- ' '")
            else:
                logging.debug("".join(self.output_buffer) + " <- " + chr(value))
            self.output_buffer.append(chr(value))

        else:
            logging.debug("".join(self.output_buffer) + " <- " + str(value))
            self.output_buffer.append(str(value))

    def get_char_from_device(self) -> int:
        self.input_buffer_pointer += 1
        return (
            ord(self.input_buffer[self.input_buffer_pointer])
            if self.input_buffer[self.input_buffer_pointer] != "0"
            else 0
        )


class ALU:
    def __init__(self):
        self.zero_flag = 0
        self.negative_flag = 0
        self.result: int = 0

    def calculate(self, operation: AluOpcode, left_op: int, right_op: int):
        result = 0
        if operation == AluOpcode.ADD:
            result = left_op + right_op
        if operation == AluOpcode.SUB or operation == AluOpcode.CMP:
            result = left_op - right_op
        if operation == AluOpcode.MUL:
            result = left_op * right_op
        if operation == AluOpcode.DIV:
            if right_op == 0:
                logging.warning("Division by zero!")
                self.result = DIVISION_BY_ZERO
                self.zero_flag = 1
                self.negative_flag = 1
                return
            result = int(left_op / right_op)
        if operation == AluOpcode.INC:
            result = left_op + 1
        self.result = result
        self.set_flags()

    def set_flags(self) -> None:
        self.zero_flag = 1 if self.result == 0 else 0
        self.negative_flag = 1 if self.result < 0 else 0

    def get_flags(self) -> list[int]:
        return [self.zero_flag, self.negative_flag]


class DataPath:
    alu = ALU()

    def __init__(self, memory_size: int, code: list, input_file: str):
        self.memory_size = memory_size
        assert memory_size > 0, "Data memory size should be non-zero"
        assert len(code) <= INPUT_OUTPUT_BUFFER_START, "Too many commands"
        self.acc = 0
        self.ip = 0
        self.dr = 0
        self.ar = 0
        self.sp = self.memory_size - 1
        self.cr: Instruction | None = None
        self.memory = code + [0] * (memory_size - len(code))
        self.left_op = 0
        self.right_op = 0
        self.device = Device(input_file)

    def latch_acc(self, sel: Selector) -> None:
        assert sel in {
            Selector.FROM_ALU,
            Selector.FROM_DR,
            Selector.FROM_STACK,
        }, "internal error, incorrect selector: {}".format(sel)
        if sel == Selector.FROM_ALU:
            self.acc = self.alu.result
        if sel == Selector.FROM_DR:
            self.acc = self.dr.arg1 if isinstance(self.dr, Instruction) else self.dr
        if sel == Selector.FROM_STACK:
            self.acc = self.memory[self.sp]

    def latch_ip(self, sel: Selector) -> None:
        assert sel in {Selector.FROM_IP, Selector.FROM_DR}, "internal error, incorrect selector: {}".format(sel)
        if sel == Selector.FROM_IP:
            self.ip = self.ip + 1
        if sel == Selector.FROM_DR:
            self.ip = self.dr
        if self.ip >= MEMORY_SIZE:
            self.ip = 0

    def latch_ar(self, sel: Selector) -> None:
        assert sel in {Selector.FROM_CR, Selector.FROM_MEMORY}, "internal error, incorrect selector: {}".format(sel)
        if sel == Selector.FROM_CR:
            self.ar = self.cr.arg2
        if sel == Selector.FROM_MEMORY:
            self.ar = self.memory[self.ar]

    def latch_sp(self, sel: Selector) -> None:
        assert sel in {Selector.SEL_INC, Selector.SEL_DEC}, "internal error, incorrect selector: {}".format(sel)
        if sel == Selector.SEL_INC:
            self.sp += 1
        if sel == Selector.SEL_DEC:
            self.sp -= 1
        if self.sp >= self.memory_size or self.sp < 0:
            self.sp = self.memory_size - 1

    def latch_dr(self, sel: Selector) -> None:
        assert sel in {Selector.FROM_CR, Selector.FROM_MEMORY}, "internal error, incorrect selector: {}".format(sel)
        if sel == Selector.FROM_CR:
            self.dr = self.cr.arg2
        if sel == Selector.FROM_MEMORY:
            self.dr = self.process_ld_operation()

    def latch_cr(self) -> None:
        self.cr = self.memory[self.ip]

    def latch_left_op(self) -> None:
        self.left_op = self.acc

    def latch_right_op(self) -> None:
        self.right_op = self.dr

    def signal_wr(self, sel: Selector):
        assert sel in {Selector.FROM_AR, Selector.FROM_SP}, "internal error, incorrect selector: {}".format(sel)
        if sel == Selector.FROM_AR:
            self.process_st_operation()
        if sel == Selector.FROM_SP:
            self.memory[self.sp] = self.acc

    def calculate(self, operation: Opcode):
        self.alu.calculate(opcode_to_alu_opcode.get(operation), self.left_op, self.right_op)

    def get_flags(self) -> list[int]:
        return self.alu.get_flags()

    def process_ld_operation(self) -> int:
        if INPUT_OUTPUT_BUFFER_START <= self.ar <= INPUT_OUTPUT_BUFFER_END:
            return self.device.get_char_from_device()
        return self.memory[self.ar]

    def process_st_operation(self) -> None:
        if INPUT_OUTPUT_BUFFER_START <= self.ar <= INPUT_OUTPUT_BUFFER_END:
            if self.acc == 1 and self.device.output_type != "int":
                self.device.output_type = "int"
                return
            self.device.output(self.acc)
            if self.device.output_type == "int":
                self.device.output_type = "str"
        else:
            self.memory[self.ar] = self.acc
