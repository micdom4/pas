export const formatDate = (date: Date | null) => {
    if (!date) return '-';
    return new Date(date).toLocaleDateString('pl-PL') + ' ' + new Date(date).toLocaleTimeString('pl-PL',
        {hour: '2-digit', minute: '2-digit'});
};