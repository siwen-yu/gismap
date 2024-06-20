#!/bin/bash

docker build -t gismap-backend:1.0.0 backend
docker tag gismap-backend:1.0.0 registry.cn-hangzhou.aliyuncs.com/yusiwen12138/gismap-backend:1.0.0
docker push registry.cn-hangzhou.aliyuncs.com/yusiwen12138/gismap-backend:1.0.0

docker build -t gismap-frontend:1.0.0 frontend
docker tag gismap-frontend:1.0.0 registry.cn-hangzhou.aliyuncs.com/yusiwen12138/gismap-frontend:1.0.0
docker push registry.cn-hangzhou.aliyuncs.com/yusiwen12138/gismap-frontend:1.0.0