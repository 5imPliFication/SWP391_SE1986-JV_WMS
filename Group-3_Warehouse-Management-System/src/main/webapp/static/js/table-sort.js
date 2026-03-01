/**
 * Table Sorting Utility
 * 
 * Makes table headers clickable to sort data via backend sorting.
 * Preserves pagination and filter parameters when sorting.
 * 
 * Usage:
 * 1. Add data-sort="columnName" attribute to sortable <th> elements
 * 2. Call TableSort.init() when page loads
 * 3. Ensure backend supports sortBy and sortDir parameters
 */

const TableSort = {
    /**
     * Initialize table sorting on all tables with sortable headers
     */
    init: function() {
        const sortableHeaders = document.querySelectorAll('th[data-sort]');
        
        sortableHeaders.forEach(header => {
            header.style.cursor = 'pointer';
            header.style.userSelect = 'none';
            
            // Add visual indicator
            const icon = document.createElement('i');
            icon.className = 'fas fa-sort ms-1 text-muted';
            icon.style.fontSize = '0.8em';
            header.appendChild(icon);
            
            // Add click handler
            header.addEventListener('click', function() {
                const sortField = this.getAttribute('data-sort');
                TableSort.sortBy(sortField);
            });
        });
        
        // Update current sort indicator
        TableSort.updateSortIndicators();
    },
    
    /**
     * Sort table by specified field
     * @param {string} field - The field name to sort by
     */
    sortBy: function(field) {
        const urlParams = new URLSearchParams(window.location.search);
        const currentSort = urlParams.get('sortBy');
        const currentDir = urlParams.get('sortDir') || 'desc';
        
        let newDir = 'asc';
        
        // If clicking same column, toggle direction
        if (currentSort === field) {
            newDir = currentDir === 'asc' ? 'desc' : 'asc';
        } else {
            // Default to descending for new column
            newDir = 'desc';
        }
        
        // Update URL parameters
        urlParams.set('sortBy', field);
        urlParams.set('sortDir', newDir);
        urlParams.set('pageNo', '1'); // Reset to first page when sorting
        
        // Navigate to new URL
        window.location.search = urlParams.toString();
    },
    
    /**
     * Update visual indicators to show current sort state
     */
    updateSortIndicators: function() {
        const urlParams = new URLSearchParams(window.location.search);
        const currentSort = urlParams.get('sortBy');
        const currentDir = urlParams.get('sortDir') || 'desc';
        
        document.querySelectorAll('th[data-sort]').forEach(header => {
            const icon = header.querySelector('i');
            if (!icon) return;
            
            const field = header.getAttribute('data-sort');
            
            if (field === currentSort) {
                // This column is being sorted
                icon.className = currentDir === 'asc' 
                    ? 'fas fa-sort-up ms-1 text-primary' 
                    : 'fas fa-sort-down ms-1 text-primary';
            } else {
                // Not sorted
                icon.className = 'fas fa-sort ms-1 text-muted';
            }
        });
    }
};

// Auto-initialize when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    TableSort.init();
});
