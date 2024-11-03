require('dotenv').config();

const express = require('express');
const cors = require('cors');
const morgan = require('morgan');
const path = require('path');

const router = require('./controller/router'); 
const userRouter = require('./controller/userRouter');

// const eurekaClient = require('./eureka-client');


const PORT = process.env.PORT || 38081;

const corsOptions = {
    origin: '*', // 모든 도메인 허용
    methods: 'GET,HEAD,PUT,PATCH,POST,DELETE',
    allowedHeaders: ['Content-Type', 'Authorization'],
  };

const app = express();


app.use(morgan('dev'));
app.use(express.json());
app.use(cors(corsOptions));

app.use('/', router);
app.use('/user', userRouter);   

app.use('/public', express.static(path.join(__dirname, 'src/public')));


app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
    
    // eurekaClient.start(error => {
    //     if (error) {
    //         console.error('Eureka 클라이언트 연결 오류:', error);
    //         console.error('에러 세부 정보:', {
    //             message: error.message,
    //             stack: error.stack,
    //             response: error.response?.body
    //         });
    //     } else {
    //         console.log('Eureka 클라이언트에 성공적으로 연결되었습니다!');
            
    //         // 연결 상태 모니터링
    //         setInterval(() => {
    //             const instances = eurekaClient.getInstancesByAppId('JAVAMSAFRONT');
    //             console.log('등록된 인스턴스:', instances);
    //         }, 10000);
    //     }
    // });
});