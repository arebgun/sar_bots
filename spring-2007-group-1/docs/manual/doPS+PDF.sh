#!/bin/sh
latex userManual && latex userManual && dvips userManual -o && ps2pdf14 userManual.ps
