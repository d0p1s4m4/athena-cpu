package Athena

import chisel3._
import org.scalatest._
import chiseltest._

class FetchTest extends flatspec.AnyFlatSpec with ChiselScalatestTester {
  "athena" should "fetch instruction from hex file" in {
    test(new Athena("src/test/hex/opcode.hex")) { cpu =>
      cpu.clock.setTimeout(25)
      while (!cpu.io.exit.peek().litToBoolean){
        cpu.clock.step(1)
      }
    }
  }
}
