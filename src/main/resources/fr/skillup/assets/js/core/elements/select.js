class Select extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({ mode: 'open' });
        this._enableSearch = false;
        this._isMultiple = false;
        this._title = 'Select options';
        this.selectedValues = [];
        this.shadowRoot.innerHTML = `
            <style>
                .select {
                    position: relative;
                    width: 200px;
                }
                .select-selected {
                    background: #fff;
                    border: 1px solid #ccc;
                    padding: 10px;
                    cursor: pointer;
                    white-space: nowrap;
                    overflow: hidden;
                    text-overflow: ellipsis;
                }
                .select-items {
                    display: none;
                    position: absolute;
                    width: 100%;
                    background: #fff;
                    border: 1px solid #ccc;
                    box-shadow: 0 2px 5px rgba(0,0,0,0.2);
                    max-height: 200px;
                    overflow-y: auto;
                }
                .filters {
                    position: sticky;
                    top: 0;
                    background: #fff;
                    padding: 5px;
                    border-bottom: 1px solid #ccc;
                    display: none;
                }
                .filters input {
                    width: 100%;
                    padding: 5px;
                    border: none;
                    border-bottom: 1px solid #ccc;
                }
                .options .category {
                    font-weight: bold;
                    padding: 5px;
                    background: #f1f1f1;
                }
                .options .option {
                    padding: 5px;
                    display: flex;
                    align-items: center;
                }
                .options .option label {
                    margin-left: 5px;
                }
                .select-items.open {
                    display: block;
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

    toggleDropdown() {
        this.itemsContainer.classList.toggle('open');
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
        const { value, checked } = event.target;
        const label = event.target.nextElementSibling.textContent;
        if (this._isMultiple) {
            if (checked) {
                this.selectedValues.push(label);
            } else {
                this.selectedValues = this.selectedValues.filter(val => val !== label);
            }
            this.selected.textContent = this.selectedValues.length ? this.selectedValues.join(', ') : this._title;
        } else {
            this.selectedValues = [label];
            this.selected.textContent = label;
            this.itemsContainer.classList.remove('open');
        }
        this.dispatchEvent(new CustomEvent('change', {
            detail: { value, label, checked }
        }));
    }

    getSelected() {
        return this._isMultiple ? [...this.selectedValues] : this.selectedValues[0];
    }
}

customElements.define('sup-select', Select);