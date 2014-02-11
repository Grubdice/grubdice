#!/bin/bash
gradle build
cp build/libs/*.war $CIRCLE_ARTIFACTS
scp build/libs/*.war deploy@grubdice.co:grubdice.war
ssh deploy@grubdice.co "/home/deploy/deploy.sh"
