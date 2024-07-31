package car.sharing

class TotalPrice {

    BigDecimal value

    static constraints = {
    }

    static final int MAX_FREE_KILOMETERS = 100
    static final BigDecimal PRICE_PER_KILOMETER = 0.1
    static final BigDecimal FEATURED_GUEST_DISCOUNT_FACTOR = 0.85

    TotalPrice(Integer days, BigDecimal perDayValue, Integer kilometers, Boolean featuredGuest) {

        def total = days * perDayValue

        def kilometersPrice = calculateKilometersPrice(kilometers)

        total = total + kilometersPrice

        total = applyFeaturedGuestDiscount(featuredGuest, total)

        this.value = total
    }

    private static BigDecimal applyFeaturedGuestDiscount(boolean featuredGuest, BigDecimal total) {
        if (featuredGuest) {
            total *= FEATURED_GUEST_DISCOUNT_FACTOR
        }
        total
    }

    private static BigDecimal calculateKilometersPrice(Integer kilometers) {
        
        if (kilometers <= MAX_FREE_KILOMETERS)
            return BigDecimal.ZERO

        ((kilometers - MAX_FREE_KILOMETERS) * PRICE_PER_KILOMETER)
    }
}
