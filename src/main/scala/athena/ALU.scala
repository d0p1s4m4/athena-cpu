package Athena

import chisel3._
import chisel3.experimental.ChiselEnum



class ALU extends Module {
    val io = IO(new Bundle {
        val a = Input(UInt(32.W))
        val b = Input(UInt(32.W))
        val op = Input(UInt(6.W))
        val result = Output(UInt(32.W))
    })

    val op = io.op
    val a = io.a
    val b = io.b
    val result = 0.U

    io.result := result
}