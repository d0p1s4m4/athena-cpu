package Athena


import chisel3._
import chisel3.util._

class Debug {
    def freg(reg: UInt) : chisel3.Printable = {
        return p"\u001b[34mr($reg)\u001b[0m"
    }

    def finstr(instr: String) : chisel3.Printable = {
        return p"\u001b[32m$instr\u001b[0m"
    }

    def fimm(value: UInt) : chisel3.Printable = {
        return p"\u001b[33m$value\u001b[0m"
    }

    def faddr(value: UInt) : chisel3.Printable = {
        return p"\u001b[35m0x${Hexadecimal(value)}\u001b[0m"
    }

    def debug(pc: UInt, inst: UInt, regfile: Mem[UInt]) = {
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

        printf(p"r1:  0x${Hexadecimal(regfile(1.U))} ")
        printf(p"r2:  0x${Hexadecimal(regfile(2.U))} ")
        printf(p"r3:  0x${Hexadecimal(regfile(3.U))} ")
        printf(p"r4:  0x${Hexadecimal(regfile(4.U))} ")
        printf(p"r5:  0x${Hexadecimal(regfile(5.U))} ")
        printf(p"r6:  0x${Hexadecimal(regfile(6.U))}\n")
        printf(p"r7:  0x${Hexadecimal(regfile(7.U))} ")
        printf(p"r8:  0x${Hexadecimal(regfile(8.U))} ")
        printf(p"r9:  0x${Hexadecimal(regfile(9.U))} ")
        printf(p"r10: 0x${Hexadecimal(regfile(10.U))} ")
        printf(p"r11: 0x${Hexadecimal(regfile(11.U))} ")
        printf(p"r12: 0x${Hexadecimal(regfile(12.U))}\n")
        printf(p"r13: 0x${Hexadecimal(regfile(13.U))} ")
        printf(p"r14: 0x${Hexadecimal(regfile(14.U))} ")
        printf(p"r15: 0x${Hexadecimal(regfile(15.U))} ")
        printf(p"r16: 0x${Hexadecimal(regfile(16.U))} ")
        printf(p"r17: 0x${Hexadecimal(regfile(17.U))} ")
        printf(p"r18: 0x${Hexadecimal(regfile(18.U))}\n")
        printf(p"r19: 0x${Hexadecimal(regfile(19.U))} ")
        printf(p"r20: 0x${Hexadecimal(regfile(20.U))} ")
        printf(p"r21: 0x${Hexadecimal(regfile(21.U))} ")
        printf(p"r22: 0x${Hexadecimal(regfile(22.U))} ")
        printf(p"r23: 0x${Hexadecimal(regfile(23.U))} ")
        printf(p"r24: 0x${Hexadecimal(regfile(24.U))}\n")

        printf(p"r25: 0x${Hexadecimal(regfile(25.U))} ")
        printf(p"r26: 0x${Hexadecimal(regfile(26.U))} ")
        printf(p"r27: 0x${Hexadecimal(regfile(27.U))} ")
        printf(p"r28: 0x${Hexadecimal(regfile(28.U))} ")
        printf(p"r29: 0x${Hexadecimal(regfile(29.U))} ")
        printf(p"r30: 0x${Hexadecimal(regfile(30.U))}\n")
        
        printf(p"r31: 0x${Hexadecimal(regfile(31.U))}\n")

        printf(p"0x${Hexadecimal(pc)} ")
        switch (opcode) {
            is (Opcodes.reginstr) {
                switch (func) {
                    is (Funcs.add) {
                        printf(p"${finstr("add")} ${freg(dest)}, ${freg(r1)}, ${freg(r2)}")
                    }
                    is (Funcs.sub) {
                        printf(p"${finstr("sub")} ${freg(dest)}, ${freg(r1)}, ${freg(r2)}")
                    }

                    is (Funcs.subu) {
                        printf(p"${finstr("subu")} ${freg(dest)}, ${freg(r1)}, ${freg(r2)}")
                    }
                }
            }

            is (Opcodes.addi) {
                printf(p"${finstr("addi")} ${freg(dest)}, ${freg(r1)}, ${fimm(imm)}")
            }

            is (Opcodes.addiu) {
                printf(p"${finstr("addiu")} ${freg(dest)}, ${freg(r1)}, ${fimm(imm)}")
            }

            is (Opcodes.andi) {
                printf(p"${finstr("andi")} ${freg(dest)}, ${freg(r1)}, ${fimm(imm)}")
            }

            is (Opcodes.call) {
                printf(p"call ${freg(dest)} ${faddr(joffset)}")
            }

            is (Opcodes.jmp) {
                printf(p"jmp ${freg(dest)} ${faddr(joffset)}")
            }
            
            is (Opcodes.lih) {
                printf(p"${finstr("lih")} ${freg(dest)}, ${freg(r1)}, ${fimm(imm)}")
            }

            is (Opcodes.lil) {
                printf(p"${finstr("lil")} ${freg(dest)}, ${freg(r1)}, ${fimm(imm)}")
            }

            is (Opcodes.subi) {
                printf(p"${finstr("subi")} ${freg(dest)}, ${freg(r1)}, ${fimm(imm)}")
            }

            is (Opcodes.ori) {
                printf(p"${finstr("ori")} ${freg(dest)}, ${freg(r1)}, ${fimm(imm)}")
            }

            is (Opcodes.nop) {
                printf("nop")
            }
        }
        printf("\n")
    }
}
