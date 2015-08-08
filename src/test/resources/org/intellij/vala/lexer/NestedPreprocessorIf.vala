#if OUTER
struct {
#if INNER
class { }
#else
const int SHOULD_NOT_APPEAR = 1;
#endif
}
#else
class {
#endif
}