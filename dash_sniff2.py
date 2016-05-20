import socket
import struct
import binascii

rawSocket = socket.socket(socket.AF_PACKET, socket.SOCK_RAW, socket.htons(0x0003))

f = open('out.txt','w')
f.write("test")
f.close()


while True:

    packet = rawSocket.recvfrom(2048)

    ethernet_header = packet[0][0:14]
    ethernet_detailed = struct.unpack("!6s6s2s", ethernet_header)

    arp_header = packet[0][14:42]
    arp_detailed = struct.unpack("2s2s1s1s2s6s4s6s4s", arp_header)

    # skip non-ARP packets
    ethertype = ethernet_detailed[2]
    if ethertype != '\x08\x06':
        continue

    print "****************Probe****************"
    print "Source MAC:      ", binascii.hexlify(ethernet_detailed[1])
    print "*************************************"
    tst = binascii.hexlify(ethernet_detailed[1]) + "\n"
    f = open('out.txt','a')
    f.write(tst)
    f.close()