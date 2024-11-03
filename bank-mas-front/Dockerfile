# 1. Node.js 베이스 이미지 설정
FROM node:18-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. package.json과 package-lock.json 파일 복사
COPY package*.json ./

# 4. 필요한 패키지 설치
RUN npm install

# 5. 애플리케이션 코드 복사
COPY . .

# 6. 애플리케이션의 포트 설정 (기본적으로 8080 포트를 사용)
EXPOSE 38081

# 7. 애플리케이션 실행 명령어
CMD ["npm", "start"]