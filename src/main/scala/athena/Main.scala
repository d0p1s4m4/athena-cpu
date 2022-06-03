package Athena

import chisel3._
import firrtl.options.TargetDirAnnotation

class Athena(memFile: String) extends Module {
    val io = IO(new Bundle {
        val exit = Output(Bool())
    })

    val core = Module(new Core());
    val memory = Module(new Memory(memFile))

    core.io.imem <> memory.io.imem
    core.io.dmem <> memory.io.dmem

    io.exit := core.io.exit
}

object Main extends App {
    val stage = new chisel3.stage.ChiselStage

    stage.execute(
        Array(
            "--target-dir", "generated"
        ),
        Seq(chisel3.stage.ChiselGeneratorAnnotation(() => new Athena("test.hex")))
    )
}