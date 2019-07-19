FROM node:10-stretch-slim

RUN apt-get update \
  && apt-get install --assume-yes --no-install-recommends \
    git \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

ARG NODE_ENV=production
RUN export NODE_ENV=${NODE_ENV}
RUN npm i -g yarn node-gyp webpack-cli
RUN mkdir /usr/app

ARG FOR_BRANCH=master
RUN git clone https://github.com/forpdi/plataforma-for.git /usr/app/repo

WORKDIR /usr/app/repo/frontend-web
RUN git checkout ${FOR_BRANCH}
RUN rm -rf dist
RUN yarn
RUN yarn build:docker
RUN rm -rf node_modules

FROM httpd

COPY --from=node /usr/app/repo/frontend-web/dist/ /usr/local/apache2/htdocs/

COPY forpdi.conf $HTTPD_PREFIX/conf/extra/forpdi.conf

EXPOSE 80
