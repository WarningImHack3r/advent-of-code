import { readFile } from "fs";

const day = __filename.split("/").pop()?.match("\\d+")?.[0];
readFile(__dirname + `/inputs/input${day}.txt`, (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const input = data.toString().split("\n").filter(x => x.length > 0);

    let Xregister = 1;
    let cycles = 0;
    let sum = 0;
    let spritePosition = 0; // MIDDLE position, length is 3
    let drawnRow: string[] = []; // lines of # or .
    input.forEach(line => {
        if (line.startsWith("addx")) { // addx
            for (let i = 0; i < 2; i++) {
                cycles++;
                sum += observeCycles(cycles, Xregister);
                draw(drawnRow, spritePosition, cycles);
            }
            Xregister += parseInt(line.split(" ")[1]);
            spritePosition = Xregister + 1;
        } else { // noop
            cycles++;
            sum += observeCycles(cycles, Xregister);
            draw(drawnRow, spritePosition, cycles);
        }
    });
    console.log(sum); // part 1
    console.log(drawnRow.join("").match(/.{1,40}/g)?.join("\n")); // part 2 (screen is slightly weird idk why but it's readable)
});

// dirty KVO, but it works
function observeCycles(cycles: number, registerValue: number): number {
    if (cycles === 20 || (cycles > 20 && (cycles - 20) % 40 === 0)) {
        // console.log("Cycle", `${cycles},`, cycles, "*", registerValue, "=", cycles * registerValue);
        return cycles * registerValue;
    }
    return 0;
}

function draw(row: string[], spritePosition: number, cycles: number) {
    row[cycles] = Array.from({ length: 3 }, (_x, i) => i + spritePosition - 1).includes(cycles % 40) ? "#" : ".";
}
