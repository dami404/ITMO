from __future__ import annotations

from collections import deque

from translator.lexer import Token
from translator.node import (
    BoolNode,
    ExpressionNode,
    IdNode,
    IfNode,
    InputOutputNode,
    InputOutputType,
    LiteralNode,
    LiteralType,
    ModuleNode,
    Node,
    NodeTypes,
    VariableInitNode,
    WhileNode,
    comp_signs,
)
from translator.token import TypeName

# Приоритеты для Обратной польской нотации
priority: dict = {"+": 1, "-": 1, "*": 2, "/": 2}


# Обратная польская нотация
def parse_math_expression(tokens: deque[Token]) -> deque[Token]:
    opn: deque[Token] = deque()
    signs: deque[Token] = deque()
    while tokens[0].token_type.name != TypeName.SEPARATOR:
        symbol = tokens.popleft()
        symbol_type = symbol.token_type.name
        process_symbol_type(symbol_type, opn, signs, symbol)
    while len(signs) > 0:
        opn.append(signs.pop())
    return opn


def process_symbol_type(symbol_type: TypeName, opn: deque[Token], signs: deque[Token], symbol: Token):
    if symbol_type == TypeName.ID or symbol_type == TypeName.NUMBER:
        opn.append(symbol)

    if symbol_type == TypeName.LEFT_BRACKET:
        signs.append(symbol)

    if symbol_type == TypeName.RIGHT_BRACKET:
        current_sign = signs.pop()
        while current_sign.token_type.name != TypeName.LEFT_BRACKET:
            opn.append(current_sign)
            current_sign = signs.pop()

    if symbol_type == TypeName.SIGN:
        sign = symbol.text
        while (
            len(signs) > 0
            and signs[-1].token_type.name != TypeName.LEFT_BRACKET
            and priority.get(sign) <= priority.get(signs[-1].text)
        ):
            opn.append(signs.pop())
        signs.append(symbol)


# Создание узла дерева для инициализации переменной
def parse_var_init(tokens: deque[Token]) -> Node:
    token = tokens.popleft()
    if token.token_type.name == TypeName.ID:
        literal_type = "default"
        variable = token.text
    else:
        literal_type = token.text
        token = tokens.popleft()
        if token.token_type.name == TypeName.LEFT_BLOCK:
            buffer = tokens.popleft().text
            tokens.popleft()
            variable = tokens.popleft().text
        else:
            variable = token.text
            buffer = 1
    tokens.popleft()
    if literal_type == "str":
        text = tokens.popleft().text[1:-1]
        return VariableInitNode(literal_type, variable, LiteralNode(LiteralType.STR, text), buffer)

    initialization_opn: deque[Token] = parse_math_expression(tokens)
    calculation: deque[Node] = deque()
    process_math_expression(initialization_opn, calculation)
    return VariableInitNode(literal_type, variable, calculation.pop())


def process_math_expression(initialization_opn: deque[Token], calculation: deque[Node]):
    while len(initialization_opn) != 0:
        new_node = Node()

        symbol = initialization_opn.popleft()
        symbol_type = symbol.token_type.name

        if symbol_type == TypeName.SIGN:
            child2 = calculation.pop()
            child1 = calculation.pop()
            new_node = ExpressionNode(symbol.text, child1, child2)

        if symbol_type == TypeName.ID:
            new_node = IdNode(symbol.text)

        if symbol_type == TypeName.NUMBER:
            new_node = LiteralNode(LiteralType.INT, int(symbol.text))

        if symbol_type == TypeName.STRING:
            new_node = LiteralNode(LiteralType.STR, symbol.text)

        calculation.append(new_node)


def parse_if_statement(tokens: deque[Token]) -> Node:
    bool_node = create_bool_statement(tokens)
    if_true = Node(NodeTypes.ROOT)
    while tokens[0].token_type.name != TypeName.RIGHT_BLOCK:
        if_true.children.append(parse_token_type(tokens))
    return IfNode(bool_node, if_true)


def parse_while_statement(tokens: deque[Token]) -> Node:
    bool_node = create_bool_statement(tokens)
    while_true_node = Node(NodeTypes.ROOT)
    while tokens[0].token_type.name != TypeName.RIGHT_BLOCK:
        while_true_node.children.append(parse_token_type(tokens))
    return WhileNode(bool_node, while_true_node)


def create_bool_statement(tokens: deque[Token]) -> BoolNode:
    tokens.popleft()
    tokens.popleft()
    literal = tokens.popleft().text
    id_node = IdNode(literal)
    next_token = tokens.popleft()

    if next_token.token_type.name == TypeName.SPEC_SYMBOL:
        module_value = tokens.popleft().text
        module_value_node = ModuleNode(LiteralNode(LiteralType.INT, int(module_value)))
        comp_sign = tokens.popleft().text
        number = tokens.popleft().text
    else:
        comp_sign = next_token.text
        number = tokens.popleft().text
        module_value_node = None
    number_node = LiteralNode(LiteralType.INT, int(number))
    bool_node = BoolNode(id_node, number_node, comp_signs.get(comp_sign), module_value_node)
    tokens.popleft()
    return bool_node


def parse_input_output_statement(tokens: deque[Token]) -> InputOutputNode:
    action = tokens.popleft().text
    if action == "print":
        input_output_type = InputOutputType.OUTPUT
    else:
        input_output_type = InputOutputType.INPUT
    tokens.popleft()
    content_token = tokens.popleft()
    content = None
    if content_token.token_type.name == TypeName.STRING:
        content = LiteralNode(LiteralType.STR, content_token.text[1:-1])
    if content_token.token_type.name == TypeName.ID:
        content = IdNode(content_token.text)
    tokens.popleft()
    return InputOutputNode(content, input_output_type)


def parse_token_type(tokens: deque[Token]) -> Node:
    token = tokens[0]
    token_type = token.token_type.name
    node = Node()
    if token_type == TypeName.TYPE or token_type == TypeName.ID:
        node = parse_var_init(tokens)

    if token_type == TypeName.IF:
        node = parse_if_statement(tokens)

    if token_type == TypeName.WHILE:
        node = parse_while_statement(tokens)

    if token_type == TypeName.PRINT or token_type == TypeName.READ:
        node = parse_input_output_statement(tokens)
    tokens.popleft()
    return node


def parse(tokens: deque[Token]) -> Node:
    tree = Node(NodeTypes.ROOT)
    while len(tokens) > 0:
        tree.children.append(parse_token_type(tokens))

    return tree
