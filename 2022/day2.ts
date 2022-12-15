import { readFile } from "fs";

readFile(__dirname + "/inputs/input2.txt", (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const content = data.toString();
    const tours = content.split("\n").filter(tour => tour.length > 0);
    const game = tours.map(tour => {
        const [him, me] = tour.split(" ");
        const myChoice = me.charCodeAt(0) - 87;
        const hisChoice = him.charCodeAt(0) - 64;
        return myChoice + (myChoice === hisChoice ? 3 : didIWin(myChoice, hisChoice) ? 6 : 0);
    }).reduce((a, b) => a + b); // part 1
    console.log(game);
    const game2 = tours.map(tour => {
        const [him, result] = tour.split(" ");
        const hisChoice = him.charCodeAt(0) - 64;
        const gameResult = result.charCodeAt(0) - 87;
        return myChoiceFrom(hisChoice, gameResult) + (gameResult === 1 ? 0 : gameResult === 2 ? 3 : 6);
    }).reduce((a, b) => a + b); // part 2
    console.log(game2);
});

/**
 * 1 = pierre
 * 2 = feuille
 * 3 = ciseaux
 */
function didIWin(me: number, him: number) {
    if ((me === 1 && him === 3) || (me === 2 && him === 1) || (me === 3 && him === 2)) return true;
    return false;
}

/**
 * 1 = perdre
 * 2 = égalité
 * 3 = gagner
 */
function myChoiceFrom(hisChoice: number, gameResult: number) {
    if (gameResult === 1) {
        if (hisChoice === 1) return 3;
        if (hisChoice === 2) return 1;
        return 2;
    }
    if (gameResult === 2) {
        if (hisChoice === 1) return 1;
        if (hisChoice === 2) return 2;
        return 3;
    }
    return hisChoice === 1 ? 2 : hisChoice === 2 ? 3 : 1;
}

