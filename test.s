ori r1 zero 100
ori r2 zero 23
add r3 r1 r2

subi r4 r3 23

bne r4 r1 7
call zero 40

addi r2 r2 23
add zero zero zero
nop

addi r2 r2 56
addi r2 r2 89

subi r2 r2 23
jmp ra 0
