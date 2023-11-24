import puppeteer from "puppeteer";
import { describe, expect, it } from "vitest"

function makeid(length) {
    var result           = '';
    var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var charactersLength = characters.length;
    for ( var i = 0; i < length; i++ ) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
   }
   return result;
}

const sleep = (milliseconds) => {
    return new Promise(resolve => setTimeout(resolve, milliseconds))
}

describe("e2e suite", async () => {
    const login = makeid(8);
    const pwd = makeid(12);
    await (async () => {
        const browser = await puppeteer.launch();
        const page = await browser.newPage();
        await page.goto('http://localhost:5173');
        // Click on register
        const [navToRegister] = await Promise.all([
            page.waitForNavigation(), // The promise resolves after navigation has finished
            page.click('button[class="btnRegister"]') // With class attribute
          ]);
        await navToRegister;

        // Register user
        await page.type('input[class="login"]', login);
        await page.type('input[class="password"]', pwd);
        await page.type('input[class="confirmpassword"]', pwd);
        const [navToLogin] = await Promise.all([
            page.waitForNavigation(),
            page.keyboard.press('Enter')
        ]);
        await navToLogin;

        // Log user in
        await page.type('input[class="login"]', login);
        await page.type('input[class="password"]', pwd);
        const [navToHome] = await Promise.all([
            page.waitForNavigation(),
            page.keyboard.press('Enter')
        ]);
        await navToHome;

        // Select emergency type
        await page.waitForSelector('select#medicalGroup');
        let url = import.meta.env.VITE_HOSPITAL_SERVICE_URL;
        url += "/hospital/specialities";
        await page.waitForResponse(url);
        await page.select('select#medicalGroup', 'Anesthésie');
        await page.select('select#speciality', 'Anesthésie');

        // Select localization
        await page.type('input#react-select-2-input', "Plymouth");
        await page.waitForNetworkIdle();
        await page.keyboard.press('Enter');
        await sleep(1000);

        // Submit form and request the nearest hospital
        await page.click('button[class="btnSubmit"]');
        await page.screenshot({path: "/home/bdelalex/images/e2e.jpg"});
        await page.waitForNetworkIdle();
        await page.waitForSelector('button[class="reserveButton"]');
        await browser.close();
    })();
    it("true", () => {
        expect(true);
    });
})