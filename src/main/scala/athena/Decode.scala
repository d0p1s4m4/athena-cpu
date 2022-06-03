package Athena

import chisel3._
import chisel3.experimental.ChiselEnum

object Opcodes {
    val reginstr = 0x0.U(6.W)
    val addi = 0x1.U(6.W)
    val addiu = 0x2.U(6.W)
    val andi = 0x5.U(6.W)
    val call = 0xD.U(6.W)
    val jmp = 0xA.U(6.W)
    val lih = 0xB.U(6.W)
    val lil = 0xC.U(6.W)
    val load = 0x9.U(6.W)
    val ori = 0x6.U(6.W)
    val nop = 0x3F.U(6.W)
    val store = 0x8.U(6.W)
    val subi = 0x3.U(6.W)
    val subiu = 0x4.U(6.W)
    val syscall = 0xE.U(6.W)
    val xori = 0x7.U(6.W)
}

object Funcs {
    val add = 0x1.U
    val addu = 0x2.U
    val and = 0x5.U
    val div = 0x6.U
    val divu = 0x7.U
    val mult = 0x8.U
    val multu = 0x9.U
    val nor = 0xA.U
    val or = 0xB.U
    val sll = 0xD.U
    val srl = 0xE.U
    val sub = 0x3.U
    val subu = 0x4.U
    val xor = 0xC.U
}
