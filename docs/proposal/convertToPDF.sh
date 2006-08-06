#!/bin/sh
latex proposal && latex proposal && dvips proposal -o && ps2pdf14 proposal.ps
