import contextlib
import io
import logging
import os
import tempfile

import pytest
from machine import processor
from translator import translate


@pytest.mark.golden_test("golden/*.yml")
def test_translator_and_machine(golden, caplog):
    caplog.set_level(logging.DEBUG)
    with tempfile.TemporaryDirectory() as tmpdirname:
        source = os.path.join(tmpdirname, "source.src")
        target = os.path.join(tmpdirname, "target.txt")
        input_stream = os.path.join(tmpdirname, "input.txt")

        with open(source, "w", encoding="utf-8") as file:
            file.write(golden["in_source"])
        with open(input_stream, "w", encoding="utf-8") as file:
            file.write(golden["in_stdin"])
        with contextlib.redirect_stdout(io.StringIO()) as stdout:
            translate.main(source, target)
            processor.main(target, input_stream)

        with open(target, encoding="utf-8") as file:
            code = file.read()
        assert code == golden.out["out_code"]
        assert stdout.getvalue() == golden.out["out_stdout"]
        assert caplog.text.strip() == golden.out["out_log"]
