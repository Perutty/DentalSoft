const rowProduct = document.querySelector('.cart-list');
const productsList = document.querySelector('#store');
const valorTotal = document.querySelector('.subtotal');
const countProduct = document.querySelector('.qty');
const countCart = document.querySelector('.quantity');
const vaciarCart = document.querySelector('#vaciar');
const pagarCart = document.querySelector('#pagar');
const activar = document.querySelector('#activar');
const bottonAction = document.querySelector('.detalleProductos');
const totalPagar = document.querySelector('.total');
const containerDetails = document.querySelector('#tablax tbody');
const botonPagar = document.querySelector('#irpagar');


//Variable de arreglo de productos
let allProducts = [];
let productosFactura = [];

if(activar){
document.addEventListener('DOMContentLoaded', () => {
        if(localStorage.getItem('productoFactura')){
       		 productosFactura = JSON.parse(localStorage.getItem('productoFactura'));
       		 botonPagar.innerText = `Pagar (${productosFactura.length})`;
       	 	 showDetails();
       	 	 showTotal();
       	 	 showHTML();
        }
	});
}

document.addEventListener('DOMContentLoaded', () => {
	if(localStorage.getItem('allProducts')){
       		 allProducts = JSON.parse(localStorage.getItem('allProducts'));
       		 showDetails();
       	 	 showTotal();
       	 	 showHTML();
       }
 });

if(vaciar){
	vaciarCart.addEventListener('click', e =>{
		allProducts.length = 0;
		localStorage.clear();
		showHTML();
	});  
}

if(pagar){
	pagarCart.addEventListener('click', e =>{
		if(allProducts.length > 0){
			window.location.href = '/detallefactura/confirmar/';
			showDetails();
		}
		else{
			alert("El carrito de compras está vacío");
		}
				showHTML();
	});
}

if(botonPagar){
	botonPagar.addEventListener('click', e =>{
			productosFactura = JSON.parse(localStorage.getItem('productoFactura'));
				let total = 0;
					productosFactura.forEach(prod =>{
					total = total+parseInt(prod.quantity * prod.price.slice(1));
					window.location.href = '/detallefactura/topay/'+prod.title+'/'+prod.quantity+'/'+prod.price.slice(1)+'/'+total+'';
			})
				localStorage.clear();
		})
	
}

if(bottonAction){
	bottonAction.addEventListener('click', e =>{
		if(e.target.classList.contains('delete')){
		
			const product = e.target.parentElement.parentElement;
			const title = product.querySelector('#na').textContent;
		
			allProducts = allProducts.filter(product => product.title !== title);
			productosFactura.forEach(prod =>{
				if(title === prod.title){
					productosFactura = productosFactura.filter(producto => producto.title !== title);
					botonPagar.innerText = `Pagar (${productosFactura.length})`;
				}
			});
			botonPagar.innerText = `Pagar (${productosFactura.length})`;
			if(allProducts.length === 0)
			{
				localStorage.clear();
				productosFactura.length=0;
			}
			showDetails();
			showTotal();
			showHTML();
		}
	});
}

if(bottonAction){
	bottonAction.addEventListener('click', e =>{
		if(e.target.classList.contains('add')){
			const product = e.target.parentElement.parentElement;
			const infoProduct = {
				title: product.querySelector('#na').textContent,
				quantity: product.querySelector('#cantidad').textContent,
				price: product.querySelector('#precio').textContent,
			};
			const exits = productosFactura.some(product => product.title === infoProduct.title);
			if(exits){
				const products = productosFactura.map(product => {
					if(product.title === infoProduct.title){
						return product
					}else{
						productosFactura = [...productosFactura, infoProduct];
						return product
					}
				})
				productosFactura = [...products];
			}else{
				
				productosFactura = [...productosFactura, infoProduct];
			}
			localStorage.setItem('productoFactura', JSON.stringify(productosFactura));
			botonPagar.innerText = `Pagar (${productosFactura.length})`;
			showDetails();
			showTotal();
			showHTML();
		}
	});
}

function showDetails (){
	//limpíarHTML
	containerDetails.innerHTML= '';
	allProducts.forEach(product => {
		if(containerDetails){
			const row =  document.createElement('tr');
			row.innerHTML += `
							 <td style="width:15%" id="img"><img src="${product.img}" style="width:70%"/></td>
							 <td id="na">${product.title}</td>
							 <td id="cantidad">${product.quantity}</td>
							 <td id="precio">$ ${parseInt(product.price.slice(1)*product.quantity)}</td>
							 <td>
								 <button id="borrar" class="delete" style="background-color:red;color:white"><i class="material-icons" data-toggle="tooltip" title="Delete">&#xE872;</i>
								 </button>
								 <button id="add" class="add" style="color:white;background-color:green"><i class="material-icons" data-toggle="tooltip" title="Approved">&#xE876;</i>
								 </button>
							 </td>
		`;
	  		containerDetails.appendChild(row);
	  	}
	});	
}


if(productsList){
	productsList.addEventListener('click', e => {
		if(e.target.classList.contains('add-to-cart-btn')){
			const product = e.target.parentElement.parentElement;
			const infoProduct = {
				img: product.querySelector('p').textContent,
				quantity: 1,
				title: product.querySelector('h3').textContent,
				price: product.querySelector('h4').textContent,
			};
			const exits = allProducts.some(product => product.title === infoProduct.title);
			if(exits){
				const products = allProducts.map(product => {
					if(product.title === infoProduct.title){
						product.quantity++;
						return product
					}else{
						return product
					}
				})
				allProducts = [...products];
			}else{
				
				allProducts = [...allProducts, infoProduct];
			}
		}
		showHTML();
	
	});
}

if(rowProduct){
	rowProduct.addEventListener('click', e =>{
		if(e.target.classList.contains('delete')){
		
			const product = e.target.parentElement;
			const title = product.querySelector('h3').textContent;
		
			allProducts = allProducts.filter(product => product.title !== title);
			showHTML();
		}
	});
}

//Funcion para mostrar HTML
function showHTML(){
	
	//limpíarHTML
	rowProduct.innerHTML= '';
	let total = 0;
	let totalOfProducts = 0;
	allProducts.forEach(product => {
		const containerProduct = document.createElement('div');
		containerProduct.innerHTML = `
		<div class="product-widget">
			<div class="product-img">
				<img src="${product.img}">
			</div>
			<div class="product-body">
			    <h3 class="product-name">${product.title}</h3>
			    <h4 class="product-price" style="color:blue"><span class="qty">${product.quantity}</span>${product.price}</h4>
			</div>
				<button class="delete"><i class="fa fa-close"></i></button>
			</div>
	  `;
	  
	  rowProduct.append(containerProduct);
	  total = total+parseInt(product.quantity * product.price.slice(1));
	  totalOfProducts = product.quantity;
	  localStorage.setItem('allProducts', JSON.stringify(allProducts));
	});
		countCart.innerText = allProducts.length;
		valorTotal.innerText = `Subtotal: $ ${total}`;
		countProduct.innerText = totalOfProducts;
};

function showTotal (){
	totalPagar.innerHTML='';
	let pagar = 0;
	allProducts.forEach(product => {
		pagar = pagar+parseInt(product.quantity * product.price.slice(1));
		totalPagar.innerText = `El total a pagar es: $ ${pagar}`;
	});
}