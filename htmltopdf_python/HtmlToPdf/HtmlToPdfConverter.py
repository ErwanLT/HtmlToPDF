import pdfkit

pdfkit.from_file('recipe.html', 'recipe.pdf', css='bootstrap.min.css')
