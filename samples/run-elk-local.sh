#!/bin/bash
# Copyright 2018, Oracle and/or its affiliates. All rights reserved.
# Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.

# start elasticsearch...
docker run -d                     \
  --name elasticsearch            \
  -p 9200:9200                    \
  -p 9300:9300                    \
  -e "discovery.type=single-node" \
  docker.elastic.co/elasticsearch/elasticsearch:6.2.2

# start kibana...
docker run -d                         \
  --name kibana                       \
  -p 5601:5601                        \
  --link elasticsearch:elasticsearch  \
  -e "ELASTICSEARCH_URL=http://elasticsearch:9200" \
  docker.elastic.co/kibana/kibana:6.2.2
