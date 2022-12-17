import { readFile } from "fs";

const day = __filename.split("/").pop()?.match("\\d+")?.[0];
readFile(__dirname + `/inputs/input${day}.txt`, (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const input = data.toString().split("\n").filter(x => x.length > 0).map(x => x.split("").map(y => parseInt(y)));
    // Goal: find the amount of trees visible in the grid
    // -> find the amount of numbers that are greater than every other number in the same row and column
    let visibleTrees = 0;
    let scenicScore = 0;
    // 1. Loop through the grid
    for (let row = 0; row < input.length; row++) {
        for (let column = 0; column < input[row].length; column++) {
            // 2. For each number, check if it is greater than every other number in the same row and column
            if (isTreeVisible(input, row, column)) {
                // 3. If it is, increase the counter
                visibleTrees++;
            }
            // 4. Calculate the scenic score of the number
            scenicScore = Math.max(scenicScore, calculateScenicScore(input, row, column));
        }
    }
    console.log(visibleTrees); // Part 1
    console.log(scenicScore); // Part 2
});

function isTreeVisible(grid: number[][], row: number, column: number): boolean {
    // Check if the number at the given row and column is greater than every other number in the same row and column
    const upperNumbers = grid.filter((_x, i) => i < row).map(x => x[column]);
    const lowerNumbers = grid.filter((_x, i) => i > row).map(x => x[column]);
    const leftNumbers = grid[row].filter((_x, i) => i < column);
    const rightNumbers = grid[row].filter((_x, i) => i > column);
    return grid[row][column] > Math.max(...upperNumbers)
        || grid[row][column] > Math.max(...lowerNumbers)
        || grid[row][column] > Math.max(...leftNumbers)
        || grid[row][column] > Math.max(...rightNumbers);
}

function calculateScenicScore(grid: number[][], row: number, column: number): number {
    // Calculate the scenic score of the number at the given row and column
    const upperNumbers = grid.filter((_x, i) => i < row).map(x => x[column]).reverse();
    const lowerNumbers = grid.filter((_x, i) => i > row).map(x => x[column]);
    const leftNumbers = grid[row].filter((_x, i) => i < column).reverse();
    const rightNumbers = grid[row].filter((_x, i) => i > column);
    // find the distance between the first number that is greater than or equel to the number at the given row and column and this number
    return [upperNumbers, lowerNumbers, leftNumbers, rightNumbers].map(x => {
        const count = x.findIndex(y => y >= grid[row][column]);
        return count === -1 ? x.length : count + 1;
    }).reduce((a, b) => a * b, 1);
}
