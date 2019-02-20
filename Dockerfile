FROM scratch
COPY summer /bin/summer
ENTRYPOINT ["/bin/summer"]