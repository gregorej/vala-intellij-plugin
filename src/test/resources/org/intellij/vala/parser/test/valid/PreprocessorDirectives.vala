namespace ContainingPreprocDirectives {

#if LINUX
            public const int OS = 1;
#elif DRAGON_FLY || FREE_BSD || NET_BSD || OPEN_BSD
            public const int OS = 2;
#endif
}