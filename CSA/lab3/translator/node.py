from __future__ import annotations

from enum import Enum


class NodeTypes(Enum):
    ROOT = "ROOT"
    VARIABLE_DECLARATION = "VARIABLE_DECLARATION"
    IF_STATEMENT = "IF_STATEMENT"
    WHILE_STATEMENT = "WHILE_STATEMENT"
    LITERAL = "LITERAL"
    ID = "ID"
    EXPRESSION = "EXPRESSION"
    DEFAULT = "DEFAULT"
    BOOL_EXPRESSION = "BOOL_EXPRESSION"
    INPUT = "INPUT"
    OUTPUT = "OUTPUT"
    MODULE = "MODULE"


class Node:
    def __init__(self, node_type: NodeTypes = NodeTypes.DEFAULT):
        self.type = node_type
        self.children: list[Node] = []

    def __str__(self):
        node_str = ""
        for node in self.children:
            node_str += str(node) + "\n"
        return node_str


class VariableInitNode(Node):
    def __init__(self, variable_type: str, variable: str, value: Node, length: int = 1, scope: str = "default"):
        Node.__init__(self, NodeTypes.VARIABLE_DECLARATION)
        self.variable_type = variable_type
        self.children.append(value)
        self.id = variable
        self.scope = scope
        self.length = length

    def __str__(self):
        return f"VariableInitNode:\n {self.variable_type} {self.length} {self.id} \n {self.children[0]}"


class LiteralType(Enum):
    STR = "STR"
    INT = "INT"
    FLOAT = "FLOAT"


class LiteralNode(Node):
    def __init__(self, literal_type: LiteralType, literal: int | float | str):
        Node.__init__(self, NodeTypes.LITERAL)
        self.type = literal_type
        self.literal = literal

    def __str__(self):
        return f"Lit Node {self.type} {self.literal}"


class IdNode(Node):
    def __init__(self, variable: str):
        Node.__init__(self, NodeTypes.LITERAL)
        self.id = variable

    def __str__(self):
        return f"Id none {self.id}"


class ExpressionNode(Node):
    def __init__(self, sign: str, child1: Node, child2: Node):
        Node.__init__(self, NodeTypes.EXPRESSION)
        self.sign = sign
        self.child1 = child1
        self.child2 = child2

    def __str__(self):
        return f"Expression node:\n child1: ( {self.child1} ) \n sign {self.sign} \n child2 ( {self.child2} )"


class CompSign(Enum):
    EQUAL = "EQUAL"
    LESS_THAN = "LESS_THAN"
    GREATER_THAN = "GREATER_THAN"


comp_signs: dict = {
    "==": CompSign.EQUAL,
    "<": CompSign.LESS_THAN,
    ">": CompSign.GREATER_THAN,
}


class ModuleNode(Node):
    def __init__(self, value: LiteralNode):
        Node.__init__(self, NodeTypes.MODULE)
        self.literal_node = value

    def __str__(self):
        return f" Module Node % {self.literal_node}"


class BoolNode(Node):
    def __init__(self, variable: IdNode, value: LiteralNode, sign: CompSign, module: ModuleNode = None):
        Node.__init__(self, NodeTypes.BOOL_EXPRESSION)
        self.children.append(variable)
        self.children.append(value)
        self.children.append(module)
        self.sign = sign

    def __str__(self):
        return f"BOOL_NODE:\n {self.children[0]} sign {self.sign} {self.children[1]} {self.children[2]}"


class IfNode(Node):
    def __init__(self, bool_statement: BoolNode, if_true: Node):
        Node.__init__(self, NodeTypes.IF_STATEMENT)
        self.children.append(bool_statement)
        self.children.append(if_true)

    def __str__(self):
        return f"IF_NODE if ({self.children[0]}) than {self.children[1]}"


class WhileNode(Node):
    def __init__(self, bool_statement: BoolNode, while_true: Node):
        Node.__init__(self, NodeTypes.WHILE_STATEMENT)
        self.children.append(bool_statement)
        self.children.append(while_true)

    def __str__(self):
        return f"WHILE_NODE ({self.children[0]}) do {self.children[1]}"


class InputOutputType(Enum):
    INPUT = "INPUT"
    OUTPUT = "OUTPUT"


input_output_types: dict[InputOutputType, NodeTypes] = {
    InputOutputType.INPUT: NodeTypes.INPUT,
    InputOutputType.OUTPUT: NodeTypes.OUTPUT,
}


class InputOutputNode(Node):
    def __init__(self, id_node: IdNode | LiteralNode, node_type: InputOutputType):
        Node.__init__(self, input_output_types.get(node_type))
        self.content = id_node

    def __str__(self):
        return f"{self.type} {self.content}"
