class Select extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: 'open'});
        this._enableSearch = false;
        this._isMultiple = false;
        this._title = 'Select options';
        this._disabled = false;
        this.selectedValues = [];
        this.selectedLabels = [];
        this.shadowRoot.innerHTML = `
            <style>
                .select {
                    position: relative;
                    width: 200px;
                }
                .select-selected {
                    background-color: #F2F0FD;
                    color: #7B69ED;
                    border: 1px solid #7B69ED;
                    border-radius: 10px;
                    padding: 10px 16px;
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    cursor: pointer;
                    box-sizing: border-box;
                }
                .select-selected.disabled {
                    background: #f1f1f1;
                    cursor: not-allowed;
                }
                .select-items {
                    display: none;
                    position: absolute;
                    border-radius: 16px;
                    width: 100%;
                    background: white;
                    max-height: 200px;
                    overflow-y: auto;
                    z-index: 9999;
                    border: 1px solid rgba(255, 255, 255, 0.3);
                    box-shadow: 0 2px 9px 0 rgba(7, 14, 194, 0.2);          
     
                }
                .filters {
                    position: sticky;
                    top: 0;
                    padding: 5px;
                    border-bottom: 1px solid #ccc; 
                    display: none;
                }
                .filters input {
                    width: 100%;
                    border: none;
                    border-bottom: 1px solid #ccc;
                    font-family: inherit;
                    
                }
                .options .category {
                    font-weight: bold;
                    padding: 10px;
                    background: #F2F0FD;
                    color: #7B69ED;
                }
                .options .option {
                    border-radius: 8px;
                    cursor: pointer;
                    font-size: 14px;
                    transition: background 0.2s ease, color 0.2s ease;
                    display: flex;
                    align-items: center;
                    background-color: white;
                    color: black;
                    padding: 10px;
                    
                }
                .options .option:hover {
                    background-color: #7B69ED;
                    color: white;
                    font-weight: bold;
                }
                
                .options .option:hover .matching-skills {
                    color: black;
                    font-weight: bold;
                }

                .options .option label {
                    margin-left: 5px;
                }
     
                .select-items.open {
                    display: block;
                }
                .options .option input[type="radio"]{
                    display: none;
                }
                .options .option input[type="checkbox"]{
                    display: none;
                }

                .select-items::-webkit-scrollbar {
                    display: none;
                }
                .options .option.selected {
                    background-color: #7B69ED;
                    color: white;
                    font-weight: bold;
                }
                
                
            </style>
            
            <div class="select">
                <div class="select-selected">${this._title}</div>
                <div class="select-items">
                    <div class="filters">
                        <input type="text" placeholder="Search..." class="select-filter">
                    </div>
                    <div class="options"></div>
                </div>
            </div>
        `;

        this.selected = this.shadowRoot.querySelector('.select-selected');
        this.itemsContainer = this.shadowRoot.querySelector('.select-items');
        this.filterInput = this.shadowRoot.querySelector('.select-filter');
        this.optionsContainer = this.shadowRoot.querySelector('.options');
        this.filterContainer = this.shadowRoot.querySelector('.filters');

        this.selected.addEventListener('click', () => this.toggleDropdown());
        this.filterInput.addEventListener('input', (e) => this.filterOptions(e.target.value));
        document.addEventListener('click', (e) => this.closeDropdown(e));
    }

    setEnableSearch(value) {
        this._enableSearch = value;
        this.filterContainer.style.display = value ? 'block' : 'none';
    }

    setMultiple(value) {
        this._isMultiple = value;
    }

    setTitle(value) {
        this._title = value;
        this.selected.textContent = value;
    }

    setDisabled(value) {
        this._disabled = value;
        this.selected.classList.toggle('disabled', value);
    }

    toggleDropdown() {
        if (this._disabled) return;
        this.itemsContainer.classList.toggle('open');
    }

    select(value) {
        const option = this.optionsContainer.querySelector(`input[value="${value}"]`);
        if (option) {
            option.checked = true;
            this.handleSelection({target: option});
        }
    }

    closeDropdown(event) {
        if (!this.contains(event.target) && !this.shadowRoot.contains(event.target)) {
            this.itemsContainer.classList.remove('open');
        }
    }

    filterOptions(searchText) {
        if (!this._enableSearch) return;
        const options = this.optionsContainer.querySelectorAll('.option');
        options.forEach(option => {
            const label = option.querySelector('label').textContent.toLowerCase();
            option.style.display = label.includes(searchText.toLowerCase()) ? 'block' : 'none';
        });
    }

    addCategory(name) {
        const category = document.createElement('div');
        category.classList.add('category');
        category.textContent = name;
        this.optionsContainer.appendChild(category);
    }

    addOption(value, label, category = null) {
        if (category) {
            let categoryElement = Array.from(this.optionsContainer.querySelectorAll('.category'))
                .find(cat => cat.textContent === category);
            if (!categoryElement) {
                this.addCategory(category);
            }
        }
        const option = document.createElement('div');
        option.classList.add('option');
        option.innerHTML = `
            <input type="${this._isMultiple ? 'checkbox' : 'radio'}" name="custom-select" id="${value}" value="${value}">
            <label for="${value}">${label}</label>
        `;
        const input = option.querySelector('input');
        input.addEventListener('change', (e) => this.handleSelection(e));
        this.optionsContainer.appendChild(option);
    }

    handleSelection(event) {
        if (this._disabled) return;
        const {value, checked} = event.target;
        const label = event.target.nextElementSibling.textContent;
        if (this._isMultiple) {
            if (checked) {
                this.selectedValues.push(value);
                this.selectedLabels.push(label);
            } else {
                this.selectedValues = this.selectedValues.filter(val => val !== value);
                this.selectedLabels = this.selectedLabels.filter(lbl => lbl !== label);
            }
            this.selected.textContent = this.hasAttribute('hide-values') ? this._title : (this.selectedLabels.length ? this.selectedLabels.join(', ') : this._title);
        } else {
            this.optionsContainer.querySelectorAll('.option').forEach(opt => {
                opt.classList.remove('selected');
            });
            this.selectedValues = [value];
            this.selectedLabels = [label];
            this.selected.textContent = this.hasAttribute('hide-values') ? this._title : label;
            this.itemsContainer.classList.remove('open');
        }
        if (!this._isMultiple) {
            event.target.closest('.option').classList.add('selected');
        }
        this.dispatchEvent(new CustomEvent('change', {
            detail: {value, label, checked}
        }));
    }

    getSelected() {
        return this._isMultiple ? [...this.selectedValues] : this.selectedValues[0];
    }

    getSelectedLabels() {
        return this._isMultiple ? [...this.selectedLabels] : this.selectedLabels[0];
    }

    getDisabled() {
        return this._disabled;
    }

    clear() {
        this.selectedValues = [];
        this.selectedLabels = [];
        this.selected.textContent = this._title;
        this.optionsContainer.querySelectorAll('.option').forEach(option => option.remove());
    }

    unselect(value) {
        const option = this.optionsContainer.querySelector(`input[value="${value}"]`);
        if (option) {
            option.checked = false;
            this.handleSelection({target: option});
        }
    }
}

customElements.define('sup-select', Select);