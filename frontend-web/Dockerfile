
FROM node:10-stretch AS build

RUN npm i -g yarn node-gyp
RUN mkdir /usr/app

ARG FOR_BRANCH=master
RUN git clone https://github.com/forpdi/plataforma-for.git /usr/app/repo

WORKDIR /usr/app/repo/frontend-web
RUN git checkout ${FOR_BRANCH}
RUN NODE_ENV=development yarn --frozen-lockfile
RUN NODE_ENV=production yarn build:docker


FROM httpd as runtime

COPY --from=build /usr/app/repo/frontend-web/dist/ /usr/local/apache2/htdocs/
COPY ./conf/apache-forpdi.conf $HTTPD_PREFIX/conf/extra/forpdi.conf
COPY ./conf/apache-httpd.conf $HTTPD_PREFIX/conf/httpd.conf

EXPOSE 80
