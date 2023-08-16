#!/bin/sh
for root in compiler core interpreter
  do
  for i in $(find $root -name "*.scala")
    do
    head -2 $i | tail -1 | grep -v Copyright && echo $i
    done
  done

