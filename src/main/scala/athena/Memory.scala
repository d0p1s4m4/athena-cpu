package Athena

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile

class IMemIO extends Bundle {
    val addr = Input(UInt(32.W))
    val inst = Output(UInt(32.W))
}

class DMemIO extends Bundle {
    val addr = Input(UInt(32.W))
    val rdata = Output(UInt(32.W))
    val wdata = Input(UInt(32.W))
    val wen = Input(Bool()) /* write enable */
}

class Memory(memFile: String) extends Module {
    val io = IO(new Bundle {
        val imem = new IMemIO()
        val dmem = new DMemIO()
    })

    val mem = Mem(16384, UInt(8.W))
    loadMemoryFromFile(mem, memFile)
    io.imem.inst := Cat(
        mem(io.imem.addr),
        mem(io.imem.addr + 1.U(32.W)),
        mem(io.imem.addr + 2.U(32.W)),
        mem(io.imem.addr + 3.U(32.W))
    )

    io.dmem.rdata := Cat(
        mem(io.dmem.addr),
        mem(io.dmem.addr + 1.U(32.W)),
        mem(io.dmem.addr + 2.U(32.W)),
        mem(io.dmem.addr + 3.U(32.W))
    )

    when(io.dmem.wen) {
        mem(io.dmem.addr) := io.dmem.wdata(7, 0)
        mem(io.dmem.addr) := io.dmem.wdata(15, 6)
        mem(io.dmem.addr) := io.dmem.wdata(23, 16)
        mem(io.dmem.addr) := io.dmem.wdata(31, 24)
    }
}
