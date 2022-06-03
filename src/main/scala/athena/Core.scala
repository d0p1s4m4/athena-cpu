package Athena

import chisel3._
import chisel3.util._
import chisel3.experimental.ChiselEnum


class Core extends Module {
    val io = IO(new Bundle {
        val exit = Output(Bool())
        val imem = Flipped(new IMemIO())
        val dmem = Flipped(new DMemIO())
    })

    val regfile = Mem(32, UInt(32.W))
    val spacialReg = Mem(3, UInt(32.W))

    /* IF Stage */
    val pc = RegInit(0.U(32.W))
    pc := pc + 4.U(32.W) /* instr are 32bit long */
    io.imem.addr := pc
    val inst = io.imem.inst

    io.dmem.addr := pc
    io.dmem.wdata := 0.U
    io.dmem.wen := 0.U

    /* ID Stage */
    val opcode = inst(5, 0)
    val func = inst(31, 26)

    val dest = inst(10, 6)
    val r1 = inst(15, 11)
    val r2 = inst(20, 16)
    val shmat = inst(25, 21)
    val imm = inst(31, 16)
    val joffset = inst(31, 11)
    val lssize = inst(17, 16)

    val r1_data = Mux(r1 =/= 0.U, regfile(r1), 0.U)
    val r2_data = Mux(r2 =/= 0.U, regfile(r2), 0.U)
    val dest_data = Mux(dest =/= 0.U, regfile(dest), 0.U)

    /* EX Stage */
    switch (opcode) {
        is (Opcodes.reginstr) {
            switch (func) {
                is (Funcs.add) {
                    regfile(dest) := r1_data + r2_data
                }

                is (Funcs.addu) {
                    regfile(dest) := r1_data + r2_data
                }

                is (Funcs.and) {
                    regfile(dest) := r1_data & r2_data
                }

                is (Funcs.div) {
                    regfile(dest) := r1_data / r2_data
                }

                is (Funcs.divu) {
                    regfile(dest) := r1_data / r2_data
                }

                is (Funcs.mult) {
                    regfile(dest) := r1_data * r2_data
                }

                is (Funcs.multu) {
                    regfile(dest) := r1_data * r2_data
                }

                is (Funcs.nor) {
                    regfile(dest) := r1_data & ~r2_data
                }

                is (Funcs.or) {
                    regfile(dest) := r1_data | r2_data
                }

                is (Funcs.sll) {
                    regfile(dest) := r1_data << shmat
                }

                is (Funcs.srl) {
                    regfile(dest) := r1_data >> shmat
                }

                is (Funcs.sub) {
                    regfile(dest) := r1_data - r2_data
                }

                is (Funcs.subu) {
                    regfile(dest) := r1_data - r2_data
                }

                is (Funcs.xor) {
                    regfile(dest) := r1_data ^ r2_data
                }
            }
        }

        is (Opcodes.addi) {
            regfile(dest) := r1_data + imm
        }

        is (Opcodes.andi) {
            regfile(dest) := r1_data & imm
        }

        is (Opcodes.call) {
            regfile(31.U) := pc + 4.U
            pc := dest_data + (joffset)
        }

        is (Opcodes.jmp) {
            pc := dest_data + (joffset)
        }

        is (Opcodes.lih) {
            regfile(dest) := (r1_data & 0xFFFF.U) | (imm << 16) 
        }

        is (Opcodes.lil) {
            regfile(dest) := (r1_data & 0xFFFF0000L.U) | imm
        }

        is (Opcodes.load) {
            switch (lssize) {
                is (0x2.U) {
                }
            }
        }

        is (Opcodes.ori) {
            regfile(dest) := r1_data | imm
        }

        is (Opcodes.nop) {
            /* do nothing */
        }

        is (Opcodes.store) {
            switch (lssize) {
                is (0x2.U) {

                }
            }
        }

        is (Opcodes.subi) {
            regfile(dest) := r1_data - imm
        }

        is (Opcodes.subiu) {
            regfile(dest) := r1_data - imm
        }

        is (Opcodes.xori) {
            regfile(dest) := r1_data ^ imm
        }
    }

    /* debug */

    if (Config.DEBUG) {
        val debug = new Debug()
        debug.debug(pc, inst, regfile)
    }

    io.exit := (inst === 0x0.U(32.W))
}
