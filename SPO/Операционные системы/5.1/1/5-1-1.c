#include <cstdint>
#include <cstdio>
#include <cstring>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <unistd.h>

#define ELF_NIDENT 16

struct elf_hdr {
    std::uint8_t e_ident[ELF_NIDENT];
    std::uint16_t e_type;
    std::uint16_t e_machine;
    std::uint32_t e_version;
    std::uint64_t e_entry;
    std::uint64_t e_phoff;
    std::uint64_t e_shoff;
    std::uint32_t e_flags;
    std::uint16_t e_ehsize;
    std::uint16_t e_phentsize;
    std::uint16_t e_phnum;
    std::uint16_t e_shentsize;
    std::uint16_t e_shnum;
    std::uint16_t e_shstrndx;
} __attribute__((packed));

std::uintptr_t entry_point(const char* name) {
    int fd = open(name, O_RDONLY);
    if (fd == -1) {
        return 0;
    }
    struct stat st;
    if (fstat(fd, &st) == -1) {
        close(fd);
        return 0;
    }
    void* mem = mmap(nullptr, st.st_size, PROT_READ, MAP_PRIVATE, fd, 0);
    if (mem == MAP_FAILED) {
        close(fd);
        return 0;
    }
    const struct elf_hdr* hdr = static_cast<const struct elf_hdr*>(mem);
    std::uintptr_t entry = hdr->e_entry;
    munmap(mem, st.st_size);
    close(fd);
    return entry;
}