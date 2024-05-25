m, q, r = [int(i) for i in input().split()]
p_mem = dict([(int(v) for v in input().split()) for i in range(m)])
l_mem = [int(input()) for i in range(q)]

def get(address):
    return p_mem[address] if address in p_mem else 0

def page(table, level, address):
    shift = [12, 21, 30, 39]
    index = (address >> shift[level]) & 0x01FF
    record = get(table + index * 8)
    if not record & 0x01:
        return None
    next_address = record & 0x000FFFFFFFFFF000
    if not level:
        return next_address
    else:
        return page(next_address, level - 1, address)

for l in l_mem:
    p = page(r, 3, l)
    if p is not None:
        print((l & 0x0FFF) | p)
    else:
        print("fault")