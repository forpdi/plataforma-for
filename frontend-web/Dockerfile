FROM node:8-alpine as node

COPY . /tmp/forpdi

WORKDIR /tmp/forpdi

RUN npm install && \
    npm run build

FROM httpd

COPY --from=node tmp/forpdi/dist  /usr/local/apache2/htdocs/

COPY forpdi.conf $HTTPD_PREFIX/conf/extra/forpdi.conf