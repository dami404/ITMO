from __future__ import annotations

import logging
import sys

from machine.control_unit import ControlUnit, DataPath
from machine.isa import MEMORY_SIZE, EndOfBufferError, HaltError, Instruction, read_code


def simulation(instructions: list[Instruction], memory_size: int, input_file: str, tick_limit: int):
    assert memory_size > 0, "Data memory size should be non-zero"
    assert tick_limit > 0, "Tick limit size should be non-zero"
    data_path = DataPath(MEMORY_SIZE, instructions, input_file)
    control_unit = ControlUnit(data_path, tick_limit)
    logging.info("Simulation start")
    try:
        while control_unit.tick_counter < tick_limit:
            control_unit.process_tick()

    except EndOfBufferError:
        logging.warning("Warning! The end of the buffer has been reached")
    except HaltError:
        logging.info("%s", "Simulation stop")

    if control_unit.tick_counter >= tick_limit:
        logging.warning("Maximum ticks reached")


def main(source: str, input_file: str):
    instructions = read_code(source)
    simulation(instructions, MEMORY_SIZE, input_file, 10000)


if __name__ == "__main__":
    logging.basicConfig(level=logging.DEBUG, format="%(levelname)-7s %(module)s:%(funcName)-13s %(message)s")
    logging.getLogger().setLevel(logging.DEBUG)
    assert len(sys.argv) == 3, "Wrong arguments: translator.py <input_file> <target_file>"
    _, source, target = sys.argv
    main(source, target)
