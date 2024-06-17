#!/bin/bash

brew services list
ps

brew services start mongodb-community
brew services start grafana
brew services start metricbeat

elasticsearch -d
kibana --silent -d

# admin/pass
mongo-express --admin --url mongodb://localhost:27017 &

brew services list
ps
