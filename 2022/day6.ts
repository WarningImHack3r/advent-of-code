import { readFile } from "fs";

const day = __filename.split("/").pop()?.match("\\d+")?.[0];
readFile(__dirname + `/inputs/input${day}.txt`, (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const input = data.toString();
    console.log(searchFirstUniqueSubstring(input, 4)); // part 1
    console.log(searchFirstUniqueSubstring(input, 14)); // part 2
});

function searchFirstUniqueSubstring(input: string, packetLength: number): number {
    for (let i = 0; i < input.length; i++) {
        const chars = input.substring(i, i + packetLength);
        if (chars.split("").every((char, index, arr) => arr.indexOf(char) === index)) {
            return i + packetLength;
        }
    }
    return -1;
}
