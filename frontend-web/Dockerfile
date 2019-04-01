FROM node:8-alpine as node

COPY . /tmp/forpdi

WORKDIR /tmp/forpdi

ARG AMBIENTE=''

RUN npm install && \
    npm install -g @angular/cli && \
    ng build $AMBIENTE --base-href=/forpdi/

FROM httpd

COPY --from=node tmp/forpdi/dist /opt/

COPY apache-custom.conf $HTTPD_PREFIX/conf/extra/forpdi.conf

RUN chgrp www-data $HTTPD_PREFIX/conf/extra/forpdi.conf && \
    echo "Include conf/extra/forpdi.conf\n" >> $HTTPD_PREFIX/conf/httpd.conf
