import { readFile } from "fs";

const day = __filename.split("/").pop()?.match("\\d+")?.[0];
readFile(__dirname + `/inputs/input${day}.txt`, (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const input = data.toString().split("\n").filter(x => x.length > 0);
    // Goal: count the number of unique positions the tail of the rope went through
    // The tail follows the head in a radius of 1. The head moves in a radius of 1 and can overlap the tail.
    // The head can move in 4 directions: U, D, L, R. The tail can move in 8 directions: U, D, L, R, UL, UR, DL, DR.
    // We'll assume that both head and tail start at the origin (0, 0).
    console.log(`The tail went through ${positionsForLastKnot(input, [0, 0], [0, 0]).size + 1} unique positions`); // part 1 (+1 for the origin?)
    // Same thing as above, but this time the entire rope is 10 knots long, each knot following the previous one if their distance is greater than 1.
    // Track the positions of the 10 knots and store the positions the last knot went through in tailPositions.
    // Finally, log the number of unique positions the last knot went through.
    const knotsX = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    const knotsY = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    console.log(`The tail went through ${positionsForLastKnot(input, knotsX, knotsY).size + 1} unique positions`); // part 2 (+1 for the origin?)
});

function positionsForLastKnot(input: string[], knotsX: number[], knotsY: number[]): Set<string> {
    const positions = new Set<string>();
    input.forEach(line => {
        const direction = line.split(" ")[0]; // U, D, L, R
        const count = parseInt(line.split(" ")[1]); // 1...?
        for (let i = 0; i < count; i++) {
            switch (direction) {
                case "U":
                    knotsY[0]++;
                    break;
                case "D":
                    knotsY[0]--;
                    break;
                case "L":
                    knotsX[0]--;
                    break;
                case "R":
                    knotsX[0]++;
                    break;
                default:
                    throw new Error(`Unknown direction: ${direction}`);
            }
            console.log(`Head moved to (${knotsX[0]}, ${knotsY[0]}) (${direction})`);
            // Move the knots
            for (let j = 1; j < knotsX.length; j++) {
                const xDiff = knotsX[j - 1] - knotsX[j];
                const yDiff = knotsY[j - 1] - knotsY[j];
                if (Math.abs(xDiff) > 1 || Math.abs(yDiff) > 1) {
                    if (xDiff > 0) {
                        knotsX[j]++;
                    } else if (xDiff < 0) {
                        knotsX[j]--;
                    }
                    if (yDiff > 0) {
                        knotsY[j]++;
                    } else if (yDiff < 0) {
                        knotsY[j]--;
                    }
                    if (j === knotsX.length - 1) {
                        positions.add(`${knotsX[j]},${knotsY[j]}`);
                    }
                    console.log(`Knot ${j} moved to (${knotsX[j]}, ${knotsY[j]})`);
                } else {
                    console.log(`Knot ${j} is close enough to the head`);
                }
            }
        }
    });
    return positions;
}
