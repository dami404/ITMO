from __future__ import annotations

import sys

from machine.isa import (
    INPUT_OUTPUT_BUFFER_END,
    INPUT_OUTPUT_BUFFER_START,
    MEMORY_SIZE,
    Instruction,
    Opcode,
    TypesOfAddressing,
    write_code,
)

from translator.ast import parse
from translator.lexer import Lexer
from translator.node import (
    BoolNode,
    CompSign,
    ExpressionNode,
    IdNode,
    IfNode,
    InputOutputNode,
    LiteralNode,
    LiteralType,
    ModuleNode,
    Node,
    NodeTypes,
    VariableInitNode,
    WhileNode,
)

commands: list[Instruction] = []
stack: dict[str, int] = {}
stack_pointer = 0
memory_pointer = INPUT_OUTPUT_BUFFER_END + 1
input_output_pointer = INPUT_OUTPUT_BUFFER_START
math_sign_to_opcode = {"+": Opcode.ADD, "-": Opcode.SUB, "*": Opcode.MUL, "/": Opcode.DIV}


def add_instructions(opcode: Opcode, arg1: int | str | None, arg2: int | str | None) -> None:
    commands.append(Instruction(len(commands), opcode, arg1, arg2))


def add_ld_direct_instr(arg: int) -> None:
    add_instructions(Opcode.LD, TypesOfAddressing.DIRECT.value, arg)


def add_ld_relative_instr(address: int) -> None:
    add_instructions(Opcode.LD, TypesOfAddressing.RELATIVE_REG.value, address)


def add_st_relative_instr(address: int) -> None:
    add_instructions(Opcode.ST, TypesOfAddressing.RELATIVE_REG.value, address)


def add_math_instr(sign: str, type_of_addressing: TypesOfAddressing, val1: int, val2: int | str = "") -> None:
    sign_opcode = math_sign_to_opcode.get(sign)
    add_instructions(sign_opcode, type_of_addressing.value + str(val2), val1)


def add_push_instr() -> None:
    global stack_pointer
    add_instructions(Opcode.PUSH, None, None)
    stack_pointer -= 1


def add_pop_instr() -> None:
    global stack_pointer
    add_instructions(Opcode.POP, None, None)
    stack_pointer += 1


def add_st_instr(address: int) -> None:
    add_instructions(Opcode.ST, TypesOfAddressing.ABSOLUT.value, address)


def add_ld_instr(address: int) -> None:
    add_instructions(Opcode.LD, TypesOfAddressing.ABSOLUT.value, address)


def add_output_instr() -> None:
    global input_output_pointer
    assert input_output_pointer <= INPUT_OUTPUT_BUFFER_END, "Invalid output adr"
    assert input_output_pointer >= INPUT_OUTPUT_BUFFER_START, "Invalid output adr"
    add_st_instr(input_output_pointer)
    input_output_pointer += 1


def reset_to_start_value_pointer():
    global input_output_pointer
    input_output_pointer = INPUT_OUTPUT_BUFFER_START


def process_first_child(child1: Node):
    if isinstance(child1, ExpressionNode):
        process_expression_node(child1)
    elif isinstance(child1, LiteralNode):
        add_ld_direct_instr(child1.literal)
    elif isinstance(child1, IdNode):
        add_ld_instr(MEMORY_SIZE + stack.get(child1.id))


def process_second_child(child2: Node, sign: str):
    if isinstance(child2, ExpressionNode):
        process_expression_node(child2)
        if sign == "+" or sign == "-" or sign == "/":
            add_st_instr(memory_pointer)
            add_pop_instr()
            add_math_instr(sign, TypesOfAddressing.ABSOLUT, memory_pointer)

    elif isinstance(child2, LiteralNode):
        add_math_instr(sign, TypesOfAddressing.DIRECT, child2.literal)
    elif isinstance(child2, IdNode):
        add_math_instr(sign, TypesOfAddressing.ABSOLUT, MEMORY_SIZE + stack.get(child2.id))


def process_expression_children(child1: Node, child2: Node, sign: str) -> None:
    process_first_child(child1)
    if (isinstance(child2, ExpressionNode)) and (sign == "+" or sign == "-" or sign == "/"):
        add_push_instr()
    process_second_child(child2, sign)


def process_expression_node(node: ExpressionNode) -> None:
    child1 = node.child1
    child2 = node.child2
    if isinstance(child1, IdNode) and isinstance(child2, IdNode) and stack[child1.id] > 0:
        process_input_str(child1, child2)
        return
    process_expression_children(child1, child2, node.sign)


def process_input_str(child1: IdNode, child2: IdNode):
    add_ld_direct_instr(stack[child1.id])
    add_push_instr()
    #
    add_ld_relative_instr(stack_pointer + MEMORY_SIZE)
    add_instructions(Opcode.CMP, TypesOfAddressing.DIRECT.value, 0)
    add_instructions(Opcode.JE, TypesOfAddressing.DIRECT.value, len(commands) + 5)
    add_pop_instr()
    add_instructions(Opcode.INC, None, None)
    add_push_instr()
    add_instructions(Opcode.JUMP, TypesOfAddressing.DIRECT.value, len(commands) - 6)
    add_ld_instr(MEMORY_SIZE + stack[child2.id])
    add_st_relative_instr(stack_pointer + MEMORY_SIZE)
    add_pop_instr()


def process_literal_node(node: LiteralNode) -> None:
    literal_type = node.type
    if literal_type == LiteralType.INT:
        add_ld_direct_instr(node.literal)
    if literal_type == LiteralType.STR:
        literal = node.literal
        add_ld_direct_instr(memory_pointer)
        add_push_instr()
        chars = [ord(c) for c in literal]
        for char in chars:
            add_literal_st_instr(char)
        add_literal_st_instr(0)


def add_literal_st_instr(char: int) -> None:
    global memory_pointer
    add_ld_direct_instr(char)
    add_st_instr(memory_pointer)
    memory_pointer += 1


def process_variable_init_mode(node: VariableInitNode) -> None:
    global memory_pointer
    variable = node.id
    expression = node.children[-1]
    temp_memory_pointer = memory_pointer
    if isinstance(expression, LiteralNode):
        process_literal_node(expression)
    else:
        translate_node(expression)
    literal_type = node.variable_type

    if literal_type == "default":
        if stack.get(variable) > 0:
            return
        add_st_instr(MEMORY_SIZE + stack.get(variable))
    elif literal_type == "int":
        add_push_instr()
        stack[variable] = stack_pointer
    elif literal_type == "str":
        literal_node = node.children[0]
        assert isinstance(literal_node, LiteralNode)
        stack[variable] = temp_memory_pointer
        memory_pointer += int(node.length) + 1


def process_id_node(node: IdNode) -> None:
    add_ld_instr(stack.get(node.id))


def process_bool_node(node: BoolNode) -> None:
    id_node = node.children[0]
    value_node = node.children[1]
    module_node = node.children[2]
    assert isinstance(id_node, IdNode)
    assert isinstance(value_node, LiteralNode)
    variable = id_node.id
    value = value_node.literal
    offset = stack.get(variable) if stack.get(variable) > 0 else MEMORY_SIZE + stack.get(variable)
    if module_node is not None:
        assert isinstance(module_node, ModuleNode)
        module_value = module_node.literal_node

        add_ld_instr(offset)
        add_push_instr()
        add_instructions(Opcode.DIV, TypesOfAddressing.DIRECT.value, module_value.literal)
        add_instructions(Opcode.MUL, TypesOfAddressing.DIRECT.value, module_value.literal)
        add_st_instr(memory_pointer)
        add_pop_instr()
        add_math_instr("-", TypesOfAddressing.ABSOLUT, memory_pointer)
        add_instructions(Opcode.CMP, TypesOfAddressing.DIRECT.value, value)
    else:
        add_ld_instr(offset)
        add_instructions(Opcode.CMP, TypesOfAddressing.DIRECT.value, value)


def process_while_node(node: WhileNode) -> None:
    condition = node.children[0]
    assert isinstance(condition, BoolNode)
    current_command_len = len(commands)
    process_bool_node(condition)
    process_new_block(node, condition.sign)
    add_instructions(Opcode.JUMP, TypesOfAddressing.DIRECT.value, current_command_len)


def process_if_node(node: IfNode) -> None:
    condition = node.children[0]
    assert isinstance(condition, BoolNode)
    process_bool_node(condition)
    process_new_block(node, condition.sign)


def process_new_block(node: Node, sign: CompSign):
    global commands
    temporary_commands: list[Instruction] = commands.copy()
    commands.clear()
    translate(node.children[1])
    temporary_commands1: list[Instruction] = commands.copy()
    commands = temporary_commands.copy()
    offset = 1 if isinstance(node, WhileNode) else 0
    if sign == CompSign.EQUAL:
        add_instructions(
            Opcode.JNE, TypesOfAddressing.DIRECT.value, len(temporary_commands1) + len(commands) + 1 + offset
        )
    if sign == CompSign.GREATER_THAN:
        add_instructions(
            Opcode.JB, TypesOfAddressing.DIRECT.value, len(temporary_commands1) + len(commands) + 1 + offset
        )
    if sign == CompSign.LESS_THAN:
        add_instructions(
            Opcode.JA, TypesOfAddressing.DIRECT.value, len(temporary_commands1) + len(commands) + 1 + offset
        )
    for inst in temporary_commands1:
        if (
            inst.opcode == Opcode.JA
            or inst.opcode == Opcode.JE
            or inst.opcode == Opcode.JB
            or inst.opcode == Opcode.JNE
        ):
            add_instructions(inst.opcode, inst.arg1, int(inst.arg2) - inst.index + len(commands))
        elif inst.opcode == Opcode.JUMP:
            add_instructions(inst.opcode, inst.arg1, int(inst.arg2) - inst.index + len(commands))
        else:
            add_instructions(inst.opcode, inst.arg1, inst.arg2)


def process_input_node(node: InputOutputNode) -> None:
    content = node.content
    assert isinstance(content, IdNode)
    add_ld_instr(INPUT_OUTPUT_BUFFER_START)
    add_st_instr(stack[content.id] + MEMORY_SIZE)


def process_output_node(node: InputOutputNode) -> None:
    content = node.content
    if isinstance(content, LiteralNode):
        chars = [ord(c) for c in content.literal]
        first_char = len(commands) + 1
        add_instructions(Opcode.JUMP, TypesOfAddressing.DIRECT.value, len(commands) + len(chars) + 2)
        for char in chars:
            add_instructions(Opcode.NOP, char, None)
        add_instructions(Opcode.NOP, 0, None)

        add_ld_direct_instr(first_char)
        add_push_instr()

        add_ld_relative_instr(MEMORY_SIZE + stack_pointer)
        add_st_instr(INPUT_OUTPUT_BUFFER_START)
        add_instructions(Opcode.CMP, TypesOfAddressing.DIRECT.value, 0)
        add_instructions(Opcode.JE, TypesOfAddressing.DIRECT.value, len(commands) + 5)
        add_pop_instr()
        add_instructions(Opcode.INC, None, None)
        add_push_instr()
        add_instructions(Opcode.JUMP, TypesOfAddressing.DIRECT.value, len(commands) - 7)

    if isinstance(content, IdNode):
        address_offset = stack.get(content.id)
        if address_offset < 0:
            add_ld_direct_instr(1)
            add_output_instr()
            add_ld_instr(MEMORY_SIZE + address_offset)
            add_output_instr()
            add_ld_direct_instr(0)
            add_output_instr()
        else:
            add_ld_direct_instr(address_offset)
            add_push_instr()

            instr_pointer = len(commands)

            add_ld_relative_instr(MEMORY_SIZE + stack_pointer)
            add_st_instr(INPUT_OUTPUT_BUFFER_START)

            add_instructions(Opcode.CMP, TypesOfAddressing.DIRECT.value, 0)
            add_instructions(Opcode.JE, TypesOfAddressing.DIRECT.value, len(commands) + 5)
            add_pop_instr()
            add_instructions(Opcode.INC, None, None)
            add_push_instr()
            add_instructions(Opcode.JUMP, TypesOfAddressing.DIRECT.value, instr_pointer)
            add_pop_instr()


def process_input_output_node(node: InputOutputNode) -> None:
    if node.type == NodeTypes.INPUT:
        process_input_node(node)
    if node.type == NodeTypes.OUTPUT:
        process_output_node(node)


def translate_node(ast_tree: Node) -> None:
    if isinstance(ast_tree, VariableInitNode):
        process_variable_init_mode(ast_tree)
    if isinstance(ast_tree, ExpressionNode):
        process_expression_node(ast_tree)
    if isinstance(ast_tree, IdNode):
        process_id_node(ast_tree)
    if isinstance(ast_tree, IfNode):
        process_if_node(ast_tree)
    if isinstance(ast_tree, WhileNode):
        process_while_node(ast_tree)
    if isinstance(ast_tree, InputOutputNode):
        process_input_output_node(ast_tree)


def translate(ast_tree: Node):
    node_type = ast_tree.type
    if node_type == NodeTypes.ROOT:
        children = ast_tree.children
        for node in children:
            translate_node(node)


def reset_param():
    global stack_pointer, memory_pointer, input_output_pointer
    commands.clear()
    stack.clear()
    stack_pointer = 0
    memory_pointer = INPUT_OUTPUT_BUFFER_END + 1
    input_output_pointer = INPUT_OUTPUT_BUFFER_START


def main(source, target):
    with open(source, encoding="utf-8") as f:
        code = f.read()
    tokens = Lexer().lex_analysis(code)
    ast_tree = parse(tokens)
    translate(ast_tree)
    add_instructions(Opcode.HALT, None, None)
    write_code(target, commands)
    reset_param()


if __name__ == "__main__":
    assert len(sys.argv) == 3, "Wrong arguments: translator.py <input_file> <target_file>"
    _, source, target = sys.argv
    main(source, target)
