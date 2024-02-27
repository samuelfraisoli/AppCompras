<body style="color:white">
Crear una aplicación que simule el funcionamiento de una tienda de productos. Tendrá
los siguientes elementos:

- MainActivity: Pantalla que tendrá:
  - Un menú superior con un item Ver carrito. La pulsación de dicho item
  llevará a la pantalla SecondActivity
  - Un spinner que mostrará todas las categorías de los productos. Para ello
  tendrás que utilizar la librería volley con el siguiente json:
  https://dummyjson.com/products/categories
  - Un recycler view que mostrará todos los productos que aparezcan en el
  siguiente json: https://dummyjson.com/products . Para la carga tendrás
  que utilizar la librería volley. Cada fila del recycler view mostrará la
  imagen (cargada con glide), el nombre, precio del producto y un botón
  de añadir al carrito. La pulsación de este botón añadirá el producto al
  carrito de compra.
- SecondActivity: pantalla que tendrá
  - Un recycler view con todos los productos que estén dentro del carrito.
  - Un TextView donde aparezca el precio total de todos los productos
  - Un menú con:
    - Un item Confirmar compra: al pulsarlo aparecerá un Snackbar
    donde aparezca el texto: Enhorabuena, compra por valor de XX
    realizada
    - Un item Vaciar carrito: al pulsarlo aparecerá un Snackbar donde
    aparezca el texto carrito vaciado y desaparecerán todos los
    elementos de la lista
</body>