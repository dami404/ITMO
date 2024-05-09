from __future__ import annotations

from enum import Enum


class TypeName(Enum):
    TYPE = "TYPE"
    LEFT_BRACKET = "LEFT_BRACKET"
    RIGHT_BRACKET = "RIGHT_BRACKET"
    SPEC_SYMBOL = "SPEC_SYMBOL"
    SEPARATOR = "SEPARATOR"
    EQUAL = "EQUAL"
    NUMBER = "NUMBER"
    SIGN = "SIGN"
    GLOBAL = "GLOBAL"
    WHILE = "WHILE"
    COMP_SIGN = "COMP_SIGN"
    LEFT_BLOCK = "LEFT_BLOCK"
    RIGHT_BLOCK = "RIGHT_BLOCK"
    PRINT = "PRINT"
    READ = "READ"
    STRING = "STRING"
    ID = "ID"
    IF = "IF"


class TokenType:
    def __init__(self, name: TypeName, regex: str):
        self.name: TypeName = name
        self.regex: str = regex

    def get_regex(self) -> str:
        return self.regex


token_type_list: list[TokenType] = [
    TokenType(TypeName.TYPE, "int|str"),
    TokenType(TypeName.LEFT_BRACKET, "\\("),
    TokenType(TypeName.RIGHT_BRACKET, "\\)"),
    TokenType(TypeName.SPEC_SYMBOL, "%"),
    TokenType(TypeName.COMP_SIGN, "==|>|<"),
    TokenType(TypeName.SEPARATOR, ";"),
    TokenType(TypeName.EQUAL, "="),
    TokenType(TypeName.NUMBER, "[0-9]+"),
    TokenType(TypeName.SIGN, "\\+|\\-|\\*|\\/"),
    TokenType(TypeName.GLOBAL, "global"),
    TokenType(TypeName.WHILE, "while"),
    TokenType(TypeName.LEFT_BLOCK, "{"),
    TokenType(TypeName.RIGHT_BLOCK, "}"),
    TokenType(TypeName.IF, "if"),
    TokenType(TypeName.PRINT, "print"),
    TokenType(TypeName.READ, "read"),
    TokenType(TypeName.STRING, "'.*'"),
    TokenType(TypeName.ID, "[a-zA-Z]+"),
]


class Token:
    def __init__(self, token_type: TokenType, text: str):
        self.token_type: TokenType = token_type
        self.text: str = text
