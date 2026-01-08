document.addEventListener('DOMContentLoaded', function() {

    // admin\products.html
    document.getElementById('productForm')?.addEventListener('submit', async function(e) {
        e.preventDefault();

        // EDIT
        const form = e.target;
        const productId = document.getElementById('productId').value;
        const mode = form.dataset.mode;
        if (mode === 'edit' && productId) {
            const currentPrice = document.getElementById('productPrice').value.replace(',', '.');
            const currentStock = document.getElementById('productStock').value;
            const currentName = document.getElementById('productName').value;
            const currentDescription = document.getElementById('productDescription').value;
            const originalName = form.dataset.originalName;
            const originalDescription = form.dataset.originalDescription;
            const originalPrice = form.dataset.originalPrice;
            const originalStock = form.dataset.originalStock;

            const productData = {
                id: productId
            };

            if (currentName !== originalName) { productData.name = currentName; }
            if (currentDescription !== originalDescription) { productData.description = currentDescription; }
            if (currentPrice !== originalPrice) {  productData.price = parseFloat(currentPrice); }
            if (currentStock !== originalStock) { productData.stock = parseInt(currentStock); }

            const keysToUpdate = Object.keys(productData).filter(key => key !== 'id');

            if (keysToUpdate.length === 0) {
                alert('Nenhuma alteração detectada para o produto.');
                bootstrap.Modal.getInstance(document.getElementById('createProductModal')).hide();
                return;
            }

            fetch('/api/products', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(productData)
            })
                .then(response => {
                    if (response.ok) {
                        location.reload();
                    } else {
                        return response.json().then(err => {
                            alert(`Erro ao atualizar produto: ${err.message || response.statusText}`);
                        }).catch(() => {
                            alert(`Erro ao atualizar produto: ${response.statusText}`);
                        });
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Erro de rede ao atualizar produto.');
                });
        } else {

            // CREATE
            const productData = {
                name: document.getElementById('productName').value,
                description: document.getElementById('productDescription').value,
                price: parseFloat(document.getElementById('productPrice').value),
                stock: parseInt(document.getElementById('productStock').value),
                categories: Array.from(document.getElementById('productCategories').selectedOptions).map(opt => opt.value)
            };

            const formData = new FormData();

            const jsonBlob = new Blob([JSON.stringify(productData)], {
                type: 'application/json'
            });

            formData.append('product', jsonBlob);

            const imageFile = document.getElementById('productImage').files[0];
            if (imageFile) {
                formData.append('image', imageFile);
            }

            fetch('/api/products', {
                method: 'POST',
                body: formData
            })
                .then(async response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        const errorData = await response.json();
                        console.error('Erro na API:', errorData);
                        alert('Erro ao salvar produto. Verifique os campos.');
                    }
                })
                .catch(error => {
                    console.error('Erro de conexão:', error);
                    alert('Erro ao conectar com o servidor.');
                });
        }
    });

    document.getElementById('createProductModal')?.addEventListener('hidden.bs.modal', function () {
        document.getElementById('productId').value = '';
        document.getElementById('productName').value = '';
        document.getElementById('productPrice').value = '';
        document.getElementById('productDescription').value = '';
        document.getElementById('productStock').value = '';
        document.getElementById('productImage').value = '';

        const categorySelect = document.getElementById('productCategories');
        for (let option of categorySelect.options) {
            option.selected = false;
        }

        document.getElementById('saveProductBtn').innerHTML = 'Salvar';
        document.getElementById('createProductModalLabel').innerHTML = 'Criar Novo Produto';
    });


    // admin\categories.html
    document.getElementById('createCategoryModal')?.addEventListener('hidden.bs.modal', function () {
        document.getElementById('categoryId').value = '';
        document.getElementById('categoryForm').reset();
        document.getElementById('saveCategoryBtn').innerText = 'Salvar Categoria';
        document.getElementById('createCategoryModalLabel').innerText = 'Nova Categoria';
    });

    document.getElementById('categoryForm')?.addEventListener('submit', function (e) {
        e.preventDefault();
        const id = document.getElementById('categoryId').value;
        const name = document.getElementById('categoryName').value;
        const description = document.getElementById('categoryDescription').value;

        const categoryData = {
            name: name,
            description: description
        };

        let method = 'POST';
        let url = '/api/categories';

        if (id) {
            method = 'PUT';
            categoryData.id = id;
        }

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(categoryData)
        })
            .then(async response => {
                if (response.ok) {
                    location.reload();
                } else {
                    const errorData = await response.json();
                    alert('Erro: ' + (errorData.message || 'Falha na operação'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Erro de conexão com o servidor');
            });
    });


    // user\profile.html
    document.getElementById('editProfileForm')?.addEventListener('submit', function(event) {
        event.preventDefault();

        const userData = {
            id: document.getElementById('userId').value,
            name: document.getElementById('userName').value,
            email: document.getElementById('userEmail').value,
            phone: document.getElementById('userPhone').value,
            birthDate: document.getElementById('userBirthDate').value,
            currentPassword: document.getElementById('currentPassword').value,
            newPassword: document.getElementById('newPassword').value || null
        };

        fetch('/api/users', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        })
            .then(async response => {
                if (response.ok) {
                    window.location.reload();
                } else {
                    const errorData = await response.json();
                    alert('Erro ao atualizar: ' + (errorData.message || 'Verifique sua senha atual.'));
                }
            })
            .catch(error => {
                console.error('Erro na requisição:', error);
                alert('Erro de conexão com o servidor.');
            });
    });


    // auth\register.html
    document.getElementById('registerForm')?.addEventListener('submit', async (e) => {
        e.preventDefault();

        const userData = {
            name: document.getElementById('name').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            phone: document.getElementById('phone').value,
            birthDate: document.getElementById('birthDate').value,
            addresses: []
        };

        const addressBlocks = document.querySelectorAll('.address-item');
        addressBlocks.forEach(block => {
            userData.addresses.push({
                streetName: block.querySelector('.streetName').value,
                number: block.querySelector('.number').value,
                complement: block.querySelector('.complement').value,
                neighborhood: block.querySelector('.neighborhood').value,
                city: block.querySelector('.city').value,
                state: block.querySelector('.state').value,
                zipCode: block.querySelector('.zipCode').value
            });
        });

        try {
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData)
            });

            if (response.ok) {
                window.location.href = '/login';
            } else {
                const error = await response.json();
                alert('Erro no cadastro: ' + (error.message || 'Verifique os dados'));
            }
        } catch (err) {
            console.error('Erro na requisição:', err);
        }
    });
});


// user\cart.html
function removeItem(button) {
    if (!confirm("Remover este item do carrinho?")) return;
    const userId = button.getAttribute('data-userId');
    const itemId = button.getAttribute('data-itemId');

    const itemsToRemove = [{
        productId: itemId,
        quantity: 1
    }];

    fetch(`/api/carts/${userId}/items`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(itemsToRemove)
    })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Erro ao remover item');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Erro ao remover item');
        });
}

function clearCart(button) {
    if (!confirm("Deseja realmente esvaziar seu carrinho?")) return;
    const userId = button.getAttribute('data-userId');

    fetch(`/api/carts/${userId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Erro ao esvaziar carrinho');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Erro ao esvaziar carrinho');
        });
}


// admin\products.html
function restoreProduct(button) {
    const id = button.getAttribute('data-id');
    const name = button.getAttribute('data-name');

    if (confirm(`Tem certeza que deseja restaurar o produto "${name}"?`)) {
        fetch(`/api/products/${id}/restore`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Erro ao restaurar produto');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Erro ao restaurar produto');
            });
    }
}

function editProduct(button) {
    const id = button.getAttribute('data-id');
    const name = button.getAttribute('data-name');
    const price = button.getAttribute('data-price');
    const description = button.getAttribute('data-description');
    const stock = button.getAttribute('data-stock');
    const categoriesString = button.getAttribute('data-categories');
    const categories = categoriesString ? categoriesString.split(',') : [];

    const form = document.getElementById('productForm');
    form.dataset.originalId = id;
    form.dataset.originalName = name;
    form.dataset.originalPrice = parseFloat(price).toFixed(2);
    form.dataset.originalDescription = description;
    form.dataset.originalStock = stock;
    form.dataset.originalCategories = categories.join(',');

    document.getElementById('productId').value = id;
    document.getElementById('productName').value = name;
    document.getElementById('productPrice').value = form.dataset.originalPrice;
    document.getElementById('productDescription').value = description;
    document.getElementById('productStock').value = stock;

    const categorySelect = document.getElementById('productCategories');
    for (let option of categorySelect.options) {
        option.selected = categories.includes(option.value);
    }

    form.dataset.mode = 'edit';
    document.getElementById('saveProductBtn').innerHTML = 'Atualizar';
    document.getElementById('createProductModalLabel').innerHTML = 'Editar Produto';
    document.getElementById('productImage').required = false;

    const modal = new bootstrap.Modal(document.getElementById('createProductModal'));
    modal.show();
}

function deleteProduct(button) {
    const id = button.getAttribute('data-id');
    const name = button.getAttribute('data-name');

    if (confirm(`Tem certeza que deseja excluir o produto "${name}"?`)) {
        fetch(`/api/products/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Erro ao excluir produto');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Erro ao excluir produto');
            });
    }
}

// admin\categories.html
function restoreCategory(button) {
    const id = button.getAttribute('data-id');
    const name = button.getAttribute('data-name');

    if (confirm(`Tem certeza que deseja restaurar a categoria "${name}"?`)) {
        fetch(`/api/categories/${id}/restore`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Erro ao restaurar categoria');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Erro ao restaurar categoria');
            });
    }
}

function editCategory(button) {
    const id = button.getAttribute('data-id');
    const name = button.getAttribute('data-name');
    const description = button.getAttribute('data-description');

    document.getElementById('categoryId').value = id;
    document.getElementById('categoryName').value = name;
    document.getElementById('categoryDescription').value = description;

    document.getElementById('saveCategoryBtn').innerHTML = 'Atualizar Categoria';
    document.getElementById('createCategoryModalLabel').innerHTML = 'Editar Categoria';

    const modalElement = document.getElementById('createCategoryModal');
    const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
    modal.show();
}

function deleteCategory(button) {
    const id = button.getAttribute('data-id');
    const name = button.getAttribute('data-name');

    if (confirm(`Tem certeza que deseja excluir a categoria "${name}"?`)) {
        fetch(`/api/categories/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Erro ao excluir categoria');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Erro ao excluir categoria');
            });
    }
}

// admin\users.html
function restoreUser(button) {
    const id = button.getAttribute('data-id');

    if (confirm(`Tem certeza que deseja restaurar o usuário?`)) {
        fetch(`/api/users/${id}/restore`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Erro ao restaurar usuário');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Erro ao restaurar usuário');
            });
    }
}

function deleteUser(button) {
    const id = button.getAttribute('data-id');

    if (confirm(`Tem certeza que deseja excluir a usuário`)) {
        fetch(`/api/users/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Erro ao excluir usuário');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Erro ao excluir usuário');
            });
    }
}

// user\profile.html
function deleteUserProfile(button) {
    const id = button.getAttribute('data-id');

    fetch(`/api/users/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Erro ao excluir conta');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Erro ao excluir conta');
        });
}

// fragments.html
async function toggleFavorite(btn) {
    const userId = btn.getAttribute('data-user-id');
    const productId = btn.getAttribute('data-product-id');
    const isFavorite = btn.getAttribute('data-active') === 'true';

    if (!userId || userId === 'null') {
        window.location.href = '/login';
        return;
    }

    const url = `/api/users/${userId}/favorites/${productId}`;
    const method = isFavorite ? 'DELETE' : 'POST';

    try {
        const response = await fetch(url, { method: method });

        if (response.ok) {
            const icon = btn.querySelector('i');
            const newStatus = !isFavorite;

            btn.setAttribute('data-active', newStatus);

            icon.classList.toggle('fas');
            icon.classList.toggle('fa-regular');
            icon.classList.toggle('text-danger');
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
    }
}

async function addToCart(button) {
    const form = button.closest('form');
    const userId = form.getAttribute('data-user-id');
    const productId = form.querySelector('input[name="productId"]').value;
    const quantityInput = form.querySelector('input[name="quantity"]');

    let quantity = (quantityInput && quantityInput.value) ? quantityInput.value : 1;
    quantity = parseInt(quantity);
    if (isNaN(quantity) || quantity <= 0) {
        quantity = 1;
    }

    const payload = [{
        productId: productId,
        quantity: quantity
    }];

    try {
        const response = await fetch(`/api/carts/${userId}/items`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            window.location.href = '/cart';
            console.log("Produto adicionado ao carrinho!");
        } else {
            const errorData = await response.json();
            alert("Erro: " + (errorData.message || "Não foi possível adicionar o produto."));
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        alert("Erro de conexão com o servidor.");
    }
}

// product\details.html
async function submitReview(button) {
    const form = button.closest('form');
    const userId = form.getAttribute('data-user-id');
    const productId = form.querySelector('input[name="productId"]').value;
    const rating = document.getElementById('rating').value;
    const reviewText = document.getElementById('reviewText').value;

    const data = {
        userId: userId,
        productId: productId,
        rating: parseInt(rating),
        review: reviewText
    };

    try {
        const response = await fetch('/api/reviews', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            location.reload();
        } else {
            const errorData = await response.json();
            alert("Erro: " + (errorData.message || "Não foi possível avaliar o produto."));
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        alert("Erro de conexão com o servidor.");
    }
}

function openEditModal(button) {
    const id = button.dataset.id;
    const rating = button.dataset.rating;
    const review = button.dataset.review;

    document.getElementById('editReviewId').value = id;
    document.getElementById('editRating').value = rating;
    document.getElementById('editReviewText').value = review;

    new bootstrap.Modal(document.getElementById('editReviewModal')).show();
}

async function updateReview() {
    const data = {
        id: document.getElementById('editReviewId').value,
        rating: document.getElementById('editRating').value,
        review: document.getElementById('editReviewText').value
    };

    const response = await fetch('/api/reviews', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    if (response.ok) {
        location.reload();
    } else {
        alert('Erro ao atualizar avaliação.');
    }
}

async function deleteReview(button) {
    if (!confirm('Deseja realmente excluir esta avaliação?')) return;

    const id = button.dataset.id;

    const response = await fetch(`/api/reviews/${id}`, {
        method: 'DELETE'
    });

    if (response.ok) {
        location.reload();
    } else {
        alert('Erro ao deletar avaliação.');
    }
}

// auth\register.html
function addAddress() {
    const container = document.getElementById('addressesContainer');
    const firstAddress = container.querySelector('.address-item');
    const newAddress = firstAddress.cloneNode(true);

    newAddress.querySelectorAll('input').forEach(input => input.value = '');

    const removeBtn = document.createElement('button');
    removeBtn.className = 'btn btn-danger btn-sm mb-2';
    removeBtn.innerHTML = '<i class="fa-solid fa-trash"></i> Remover este endereço';
    removeBtn.onclick = function() { this.parentElement.remove(); };
    newAddress.prepend(removeBtn);

    container.appendChild(newAddress);
}