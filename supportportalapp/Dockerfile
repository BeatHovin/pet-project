FROM node:16.20.1

RUN mkdir -p /app

WORKDIR /app

COPY . .
RUN npm install
RUN npm run build --prod

CMD ["npm", "start"]
