#!/bin/bash

TO_FIND="$1"
shift

for i in *.jar; do
  echo $i
  unzip -l $i | grep -i "$TO_FIND"
done
