#!/bin/sh
latex writeup && latex writeup && dvips writeup -o && ps2pdf14 writeup.ps
