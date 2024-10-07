#!/bin/sh

# names of files (without extension)
files=$(ls web-client/agreements | sed -e 's/\.html$//')

# convert files
for file in $files
do
	echo "converting ${file}.html to ${file}.pdf"
	wkhtmltopdf --enable-forms --print-media-type web-client/agreements/${file}.html src/main/resources/pdfs/${file}.pdf
done
