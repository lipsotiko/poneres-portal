#!/bin/sh

# names of files (without extension)
cd web-client/agreements
files=$(ls *.html | sed -e 's/\.html$//')
cd ../..

# convert files
for file in $files
do
	echo "converting ${file}.html to ${file}.pdf"
	wkhtmltopdf --enable-local-file-access --enable-forms --print-media-type --footer-center 'Page [page] of [topage]' --footer-spacing 4 --margin-bottom 30mm web-client/agreements/${file}.html src/main/resources/pdfs/${file}.pdf
done
