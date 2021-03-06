import sys

REGISTERS = {
    'r0':  0b00000,
    'r1':  0b00001,
    'r2':  0b00010,
    'r3':  0b00011,
    'r4':  0b00100,
    'r5':  0b00101,
    'r6':  0b00110,
    'r7':  0b00111,
    'r8':  0b01000,
    'r9':  0b01001,
    'r10': 0b01010,
    'r11': 0b01011,
    'r12': 0b01100,
    'r13': 0b01101,
    'r14': 0b01110,
    'r15': 0b01111,
    'r16': 0b10000,
    'r17': 0b10001,
    'r18': 0b10010,
    'r19': 0b10011,
    'r20': 0b10100,
    'r21': 0b10101,
    'r22': 0b10110,
    'r23': 0b10111,
    'r24': 0b11000,
    'r25': 0b11001,
    'r26': 0b11010,
    'r27': 0b11011,
    'r28': 0b11100,
    'r29': 0b11101,
    'r30': 0b11110,
    'r31': 0b11111,

    # proxy name
    'zero': 0b00000,
    'sp':   0b11100,
    'gp':   0b11101,
    'fp':   0b11110,
    'ra':   0b11111
}

FUNCS = {
    'add':     0b000001,
    'addu':    0b000010,
    'and':     0b000101,
    'div':     0b000110,
    'divu':    0b000111,
    'mult':    0b001000,
    'multu':   0b001001,
    'nor':     0b001010,
    'nop':     0b000000,
    'or':      0b001011,
    'sll':     0b001101,
    'srl':     0b001110,
    'sub':     0b000011,
    'subu':    0b000100,
    'syscall': 0b111111,
    'xor':     0b001100,
}

SFUNCS = {
    'eret': 0b000001,
    'csrr': 0b111110,
    'csrw': 0b111111
}

OPCODES = {
    'addi':  0b000001,
    'andiu': 0b000010,
    'andi':  0b000101,
    'beq':   0b001110,
    'bne':   0b001111,
    'blt':   0b010000,
    'bgt':   0b010001,
    'call':  0b001101,
    'jmp':   0b001010,
    'lih':   0b001011,
    'lil':   0b001100,
    'load':  0b001001,
    'ori':   0b000110,
    'store': 0b001000,
    'subi':  0b000011,
    'subiu': 0b000100,
    'xori':  0b000111
}

def rformat(func, dest, r1, r2):
    ret = REGISTERS[dest] << 6
    ret |= REGISTERS[r1] << 11
    if func == 'sll' or func == 'srl':
        ret |= int(r2) << 21
    else:
        ret |= REGISTERS[r2] << 16
    ret |= FUNCS[func] << 26
    return ret

def rsformat(func, dest, r1, r2):
    ret = 0b111111
    ret |= SFUNCS[func] << 26
    return ret

def iformat(opcode, dest, r1, imm):
    ret = OPCODES[opcode]
    ret |= REGISTERS[dest] << 6
    ret |= REGISTERS[r1] << 11
    ret |= int(imm) << 16
    return ret

def jformat(opcode, dest, offset):
    ret = OPCODES[opcode]
    ret |= REGISTERS[dest] << 6
    ret |= int(offset) << 11
    if int(offset) > pow(2, 20):
        print(f"offset > {pow(2, 20)}")
    return ret

def lsformat(opcode, r1, r2, width, offset):
    ret = OPCODES[opcode]
    ret |= REGISTERS[r1] << 6
    ret |= REGISTERS[r2] << 11
    ret |= width << 16
    ret |= int(offset) << 18
    return ret

INSTRS = {
    'add': rformat,
    'addu': rformat,
    'addi': iformat,
    'addiu': iformat,
    'and': rformat,
    'andi': iformat,
    'b': lambda op, r1, r2, offset: iformat('beq', 'zero', 'zero', offset),
    'beq': iformat,
    'beqz': lambda op, r1, r2, offset: iformat('beq', r1, 'zero', offset),
    'bne': iformat,
    'bnez': lambda op, r1, r2, offset: iformat('bne', r1, 'zero', offset),
    'blt': iformat,
    'bltz': lambda op, r1, r2, offset: iformat('blt', r1, 'zero', offset),
    'bgt': iformat,
    'bgtz': lambda op, r1, r2, offset: iformat('bgt', r1, 'zero', offset),
    'call': jformat,
    'div': rformat,
    'divu': rformat,
    'eret': rsformat,
    'jmp': jformat,
    'lb': lambda op, r1, r2, offset: lsformat('load', r1, r2, 0b00, offset),
    'lh': lambda op, r1, r2, offset: lsformat('load', r1, r2, 0b01, offset),
    'lw': lambda op, r1, r2, offset: lsformat('load', r1, r2, 0b10, offset),
    'lih': iformat,
    'lil': iformat,
    'mult': rformat,
    'multu': rformat,
    'nor': rformat,
    'nop': lambda x: rformat('nop', 'r0', 'r0', 'r0'),
    'or': rformat,
    'ori': iformat,
    'sll': rformat,
    'srl': rformat,
    'sb': lambda op, r1, r2, offset: lsformat('store', r1, r2, 0b00, offset),
    'sh': lambda op, r1, r2, offset: lsformat('store', r1, r2, 0b01, offset),
    'sw': lambda op, r1, r2, offset: lsformat('store', r1, r2, 0b10, offset),
    'sub': rformat,
    'subu': rformat,
    'subi': iformat,
    'subiu': iformat,
    'syscall': lambda x: rformat('syscall', 'r0', 'r0', 'r0'),
    'xor': rformat,
    'xori': iformat 
}

if __name__ == '__main__':
    if len(sys.argv) < 2:
        sys.exit(-1)
    binary = []
    with open(sys.argv[1], 'rb') as f:
        for line in f:
            line = line.strip().decode('utf-8')
            if line.startswith(";") or line == "":
                continue
            token = line.split()
            binary.append(INSTRS[token[0]](*token).to_bytes(4, byteorder='big', signed=True))
    with open(sys.argv[2], 'wb') as f:
        for b in binary:
            f.write(b)
