from __future__ import annotations

import re
from collections import deque

from translator.token import Token, token_type_list


class Lexer:
    token_type_list: dict = token_type_list

    def lex_analysis(self, code) -> deque[Token]:
        token_list: deque[Token] = deque()
        while self.next_token(code) is not None:
            code = code.strip()
            token = self.next_token(code)
            token_list.append(token)
            code = code[len(token.text) :]
        return token_list

    # Поиск в начале какой-либо конструкции
    def next_token(self, code) -> Token | None:
        code = code.strip()
        for token_type in token_type_list:
            regex = token_type.get_regex()
            result = re.match(regex, code)
            if result is not None:
                return Token(token_type, result.group(0))
        return None
