import { Client } from "pg";
import { v4 as uuidv4 } from 'uuid';

async function basic() {
    const dbConnectinoString = "postgresql://testuser:postgres@localhost:55532/word_memory";

    const client = new Client(dbConnectinoString);


    try {

        client.connect();

        const username = "shrona";
        const checkUser = await client.query(`select * from _user where nickname = '${username}'`);


        // 유저 하나 추가
        if(checkUser.rows.length == 0){
            const createUserQuery = "insert into _user (user_id, login_id, nickname, password, created_at, updated_at) values (1, 'shrona', 'shrona', 'GIK5G39J1HnPHsLx7O4w0OU5LpY6IQkBW3FJv3Eq0bY=', now(), now())";
            const afterCreate =  await client.query(createUserQuery);
        }


        console.log(checkUser.rows)
        // 단어 30개 추가
        const wordList = [
            ["account","계산"]
            ,["account for","~를 설명하다"]
            ,["benefit from","~로부터 혜택을 받다"]
            ,["condense","압축하다"]
            ,["condense into","~로 압축하다"]
            ,["better","~를 개선하다"]
            ,["conduct","실시, 시행하다"]
            ,["accrue","생기다, 얻어지다"]
            ,["conductor","지휘자 , 차장"]
            ,["confirmation","확정서"]
            ,["be aware of","~를 조심하다"]
            ,["biannual","반년마다의"]
            ,["accumulation","축적, 축적물"]
            ,["confirmed","확인된"]
            ,["beneficial","혜택이 많은"]
            ,["beneficiary","수혜자"]
            ,["confiscate","압수, 몰수하다"]
            ,["binding","구속력 있는, 의무적인"]
            ,["bill","계산서, 청구서, 법안"]
            ,["accurately","정확히"]
            ,["achievement","업적, 성과"]
            ,["irretrievable", "돌이킬 수 없는"]
            ,["issue", "발행하다, 발급, 문제"]
            ,["be laid off", "해고되다"]
            ,["landfill", "매립지"]
            ,["landslide", "사태, 산사태"]
            ,["itinerary", "여행스케줄"]
            ,["agenda", "의사일정"]
            ,["mutually", "서로"]
            ,["natural disaster", "재해"]
        ]

        // console.log(wordList)
        for(const word of wordList) {

            const wordCheckQuery = await client.query(`select * from word where word = '${word[0]}'`);
            if(wordCheckQuery.rowCount == 0) {
                const wordSaveQuery = `insert into word (word_id, word, meaning, created_at, updated_at) values ('${uuidv4()}', '${word[0]}', '${word[1]}', now(), now())`
                await client.query(wordSaveQuery);
            }
        }

        const dbWordList = await client.query(`select word_id from word limit 30`);

        const wordIdList = dbWordList.rows.map(dbInfo => dbInfo.word_id);

        // 유저와 단어 연결
        for(const worddbId of wordIdList) {
            const joinCheck = await client.query(`select * from join_word_user where user_id = 1 and word_id = '${worddbId}'`);
            if(joinCheck.rowCount == 0) {
                const joinSaveQuery = `insert into join_word_user (word_user_id, created_at, updated_at, user_id, word_id) values ('${uuidv4()}', now(), now(), 1, '${worddbId}')`
                await client.query(joinSaveQuery);
                console.log("저장");
            }
            // break;
        }

    } catch(e) {
        console.log(e);
    } finally {
        client.end();
    }

}

basic();